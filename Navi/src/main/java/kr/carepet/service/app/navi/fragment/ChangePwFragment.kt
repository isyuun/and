/*
 *  * Copyright(c) 2023 CarePat All right reserved.
 *  * This software is the proprietary information of CarePet.
 *  *
 *  * Revision History
 *  * Author                         Date             Description
 *  * --------------------------    -------------    ----------------------------------------
 *  * sanghun.lee@care-biz.co.kr     2023. 7. 28.    CarePet Development...
 *  *
 */

package kr.carepet.service.app.navi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kr.carepet.service.app.navi.R

class ChangePwFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_change_pw, container, false)

        val etPw1 = view.findViewById<EditText>(R.id.cp_et_pw)
        val etPw2 = view.findViewById<EditText>(R.id.cp_et_pw2)


        val btnCancel = view.findViewById<TextView>(R.id.cp_btn_cancel)
        btnCancel.setOnClickListener { dismiss() }

        val btnConfirm = view.findViewById<TextView>(R.id.cp_btn_confirm)
        btnConfirm.setOnClickListener {
            val parentActivity = activity
            val password1 = etPw1.text.toString()
            val password2 = etPw2.text.toString()

            if(password1.equals(password2) && password1.isNotEmpty()){
                // bottomSheet 종료
                parentActivity?.let {
                    dismiss()
                    Snackbar.make(
                        parentActivity.findViewById(android.R.id.content),
                        "새로운 비밀번호: $password1",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return view
    }

}