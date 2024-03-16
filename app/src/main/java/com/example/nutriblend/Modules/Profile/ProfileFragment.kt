package com.example.nutriblend.Modules.Profile

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.nutriblend.Model.Model
import com.example.nutriblend.Model.User
import com.example.nutriblend.SigninActivity
import com.example.nutriblend.databinding.FragmentProfileBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _binding:  FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    private lateinit var profileImage: ImageView
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailTextView: TextView

    private lateinit var cancelEditProfileBtn: Button
    private lateinit var saveProfileBtn: Button
    private lateinit var editProfileFloatingActionButton: FloatingActionButton
    private lateinit var signoutBtn: Button

    private lateinit var progressBar: ProgressBar

    private var selectedImageUri: Uri? = null

    // TODO: check img logic
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                selectedImageUri = data?.data
                Picasso.get().load(selectedImageUri).into(profileImage)
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.user = Model.instance.getUserProfileById(auth.currentUser?.uid!!)

        viewModel.user?.observe(viewLifecycleOwner) {
            setupUI(it, view)
        }

        return view
    }

    private fun setupUI(user: User?, view: View) {
        bindElements()
        insertUserProfileValues(user)
        editableUi(false)
        initListeners(user)
        observeActionStatuses(view)
    }

    private fun bindElements(){
        profileImage = binding.profileCircularImageView
        firstNameEditText = binding.profileFirstnameValueEditText
        lastNameEditText = binding.profileLastnameValueEditText
        usernameEditText = binding.profileUsernameValueEditText
        emailTextView = binding.emailValueTextView

        cancelEditProfileBtn = binding.cancelEditProfileBtn
        saveProfileBtn = binding.saveProfileBtn
        editProfileFloatingActionButton = binding.editProfileFloatingActionButton
        signoutBtn = binding.profileLogoutBtn

        progressBar = binding.progressBarProfileFragment
        progressBar.visibility = View.GONE
    }

    private fun insertUserProfileValues(user: User?){
        if(user?.avatarUrl != null){
            Picasso.get().load(user.avatarUrl).into(profileImage)

        }
        firstNameEditText.setText(user?.firstName)
        lastNameEditText.setText(user?.lastName)
        usernameEditText.setText(user?.username)
        emailTextView.text = user?.email
    }

    fun initListeners(user: User?) {
        signoutBtn.setOnClickListener{
            signoutUser()
        }

        cancelEditProfileBtn.setOnClickListener{
            insertUserProfileValues(user)
            editableUi(false)
        }

        editProfileFloatingActionButton.setOnClickListener {
            editableUi(true)
        }

        saveProfileBtn.setOnClickListener {
            val userFirstName = firstNameEditText.text.toString()
            val userLastName = lastNameEditText.text.toString()
            val userName = usernameEditText.text.toString()

            viewModel.updateUserProfile(user!!,selectedImageUri,userFirstName,userLastName,userName)
        }
    }

    private fun observeActionStatuses(view: View){
        viewModel.isUserUpdatedSuccessfully.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "User ${usernameEditText.text} updated successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Failed to update User ${usernameEditText.text}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    fun editableUi(isEditable: Boolean){
        if(isEditable) {
            profileImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                getContent.launch(intent)
            }

            saveProfileBtn.visibility = View.VISIBLE
            cancelEditProfileBtn.visibility = View.VISIBLE
            editProfileFloatingActionButton.visibility = View.GONE
            signoutBtn.visibility = View.GONE

            firstNameEditText.isEnabled = true
            firstNameEditText.isFocusable = true
            firstNameEditText.isFocusableInTouchMode = true
            firstNameEditText.setTextColor(Color.BLACK)

            lastNameEditText.isEnabled = true
            lastNameEditText.isFocusable = true
            lastNameEditText.isFocusableInTouchMode = true
            lastNameEditText.setTextColor(Color.BLACK)

            usernameEditText.isEnabled = true
            usernameEditText.isFocusable = true
            usernameEditText.isFocusableInTouchMode = true
            usernameEditText.setTextColor(Color.BLACK)

            signoutBtn.isClickable = false
            signoutBtn.isActivated = false
        } else {
            saveProfileBtn.visibility = View.GONE
            cancelEditProfileBtn.visibility = View.GONE
            editProfileFloatingActionButton.visibility = View.VISIBLE
            signoutBtn.visibility = View.VISIBLE

            profileImage.setOnClickListener(null)

            firstNameEditText.isEnabled = false
            firstNameEditText.isFocusable = false
            firstNameEditText.isFocusableInTouchMode = false
            firstNameEditText.setTextColor(Color.BLACK)

            lastNameEditText.isEnabled = false
            lastNameEditText.isFocusable = false
            lastNameEditText.isFocusableInTouchMode = false
            lastNameEditText.setTextColor(Color.BLACK)

            usernameEditText.isEnabled = false
            usernameEditText.isFocusable = false
            usernameEditText.isFocusableInTouchMode = false
            usernameEditText.setTextColor(Color.BLACK)

            signoutBtn.isClickable = true
            signoutBtn.isActivated = true
        }
    }
    private fun signoutUser() {
        auth.signOut()
        val intent = Intent(requireContext(), SigninActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }
}