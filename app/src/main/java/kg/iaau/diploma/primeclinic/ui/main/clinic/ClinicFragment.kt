package kg.iaau.diploma.primeclinic.ui.main.clinic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import kg.iaau.diploma.primeclinic.R
import kg.iaau.diploma.primeclinic.databinding.FragmentClinicBinding

@AndroidEntryPoint
class ClinicFragment : Fragment() {

    private lateinit var vb: FragmentClinicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb =  FragmentClinicBinding.inflate(inflater, container, false)
        return vb.root
    }

}