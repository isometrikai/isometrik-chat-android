package io.isometrik.ui.messages.chat.contactList

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.isometrik.chat.R
import io.isometrik.chat.databinding.ActivityContactsListBinding
import org.json.JSONArray

class ContactsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityContactsListBinding
    var contactList: ArrayList<ContactSharedModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contactListString = intent.extras?.getString("contactList").orEmpty()

        if (contactListString.isNotEmpty()) {
            val array = JSONArray(contactListString)

            for (i in 0 until array.length()) {
                val contact = array.getJSONObject(i)
                contactList.add(
                    ContactSharedModel(
                        contact.getString("contactName"),
                        contact.getString("contactIdentifier"),
                        contact.getString("contactImageUrl")
                    )
                )
            }

            val adapter = ContactListAdapter(this@ContactsListActivity, contactList, object:
                ContactListAdapter.ContactListener {
                override fun onAddClick(contact: ContactSharedModel) {
                    openInsertContactScreen(contact.name,contact.identifier)
                }

                override fun onCallClick(contact: ContactSharedModel) {
                    openDialScreen(contact.identifier)
                }

                override fun onSMSClick(contact: ContactSharedModel) {
                    openSmsApp(contact.identifier)
                }
            })
            binding.rvContact.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvContact.adapter = adapter

        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun openSmsApp(contactNumber: String) {
        val smsUri = Uri.parse("smsto:$contactNumber")
        val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri)
        smsIntent.putExtra("sms_body", "Hello, this is your message")

        // Check if there is an SMS app available to handle the intent
        if (smsIntent.resolveActivity(packageManager) != null) {
            startActivity(smsIntent)
        } else {
            // Handle the case where no SMS app is installed
            // You can display a message or take alternative actions
        }
    }

    private fun openDialScreen(contactNumber: String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${contactNumber}")
        startActivity(intent)
    }

    private fun openInsertContactScreen(contactName: String,contactNumber: String){
        val intent = Intent(Intent.ACTION_INSERT)
        intent.type = ContactsContract.Contacts.CONTENT_TYPE
        intent.putExtra(ContactsContract.Intents.Insert.NAME, contactName)
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, contactNumber)
        if (intent.resolveActivity(this@ContactsListActivity.packageManager) == null) {
            Toast.makeText(
                this@ContactsListActivity, getString(R.string.ism_no_apps_installed),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            startActivity(intent)
        }
    }
}