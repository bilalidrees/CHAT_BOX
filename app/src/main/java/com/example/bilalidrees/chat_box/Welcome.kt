package com.example.bilalidrees.chat_box

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.view.ViewPager
import android.os.Bundle
import com.example.bilalidrees.chat_box.Admin.Admin
import com.example.bilalidrees.chat_box.User.User


class Welcome : AppCompatActivity() {

    private var mSectionsPageAdapter: SectionsPageAdapter? = null
    private var mViewPager: ViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        mSectionsPageAdapter = SectionsPageAdapter(supportFragmentManager)

        // Set up the Vie:Pager with the sections adapter.
        mViewPager = findViewById(R.id.container) as ViewPager
        setupViewPager(mViewPager!!)

        val tabLayout = findViewById(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(mViewPager)


    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPageAdapter(supportFragmentManager)
        adapter.addFragment(User(),"USER")
       adapter.addFragment(Admin(), "ADMIN")
        viewPager.adapter = adapter
        // finish();
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            this@Welcome.finish()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onBackPressed() {

        val a_builder = AlertDialog.Builder(this@Welcome)
        a_builder.setMessage("Do you want to Close this App !!!")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, which -> finish() }
                .setNegativeButton("No") { dialog, which -> dialog.cancel() }
        val alert = a_builder.create()
        alert.setTitle("Alert !!!")
        alert.show()


    }

}
