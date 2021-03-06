package com.example.hackatonapp.presentation.screen.data_list

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.hackatonapp.R
import com.example.hackatonapp.databinding.FragmentPatientDataListBinding
import com.example.hackatonapp.presentation.adapter.patient_data_list.PatientDataListAdapter
import com.example.hackatonapp.presentation.extensions.viewBinding

class PatientDataListFragment : Fragment(R.layout.fragment_patient_data_list) {

    private val binding by viewBinding(FragmentPatientDataListBinding::bind)

    private val viewModel: PatientDataListViewModel by viewModels()

    private val recyclerAdapter = PatientDataListAdapter().apply {
        onItemClickListener = PatientDataListAdapter.OnItemClickListener { position ->
            val action = PatientDataListFragmentDirections.navigateToAddNoteFragment()
            action.id = viewModel.onListItemClick(position)
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        initViews()
        setListeners()
        subscribeToViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun initViews() {
        binding.recyclerView.apply {
            adapter = recyclerAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }

    private fun setListeners() {
        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.navigateToAddNoteFragment)
        }
    }

    private fun subscribeToViewModel() {
        viewModel.apply {
            allNotes.observe(viewLifecycleOwner) {
                onAllNotesUpdate()
            }
            listToShow.observe(viewLifecycleOwner) {
                recyclerAdapter.submitList(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        viewModel.update(sharedPref.getString("token", "")!!)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_patient_data_list, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchMenuItem: MenuItem = menu.findItem(R.id.action_search)

        val searchView = searchMenuItem.actionView as SearchView

        searchView.queryHint = getString(R.string.patient_note_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newQuery: String): Boolean {
                viewModel.updateSearchQuery(newQuery)
                return true
            }
        })
    }
}