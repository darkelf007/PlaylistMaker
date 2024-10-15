import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.playlistmaker.app.App
import com.android.playlistmaker.databinding.FavoriteFragmentBinding
import com.android.playlistmaker.media.domain.models.FavoriteTrack
import com.android.playlistmaker.media.presentation.FavoriteFragmentViewModel
import com.android.playlistmaker.media.presentation.FavouriteTrackState
import com.android.playlistmaker.media.presentation.adapter.FavoriteTrackAdapter
import com.android.playlistmaker.player.presentation.PlayerActivity
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {
    private var adapter: FavoriteTrackAdapter? = null

    private var isClickAllowed = true

    private lateinit var emptyLibraryPlaceholder: ConstraintLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var libraryRecyclerView: RecyclerView

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun newInstance() = FavoriteFragment().apply { }
    }

    private val viewModel: FavoriteFragmentViewModel by viewModel()

    private lateinit var binding: FavoriteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteTrackAdapter { libraryTrack ->
            if (clickDebounce()) {
                clickOnItem(libraryTrack)
            }
        }

        progressBar = binding.progressBar
        libraryRecyclerView = binding.libraryRecyclerView
        emptyLibraryPlaceholder = binding.emptyLibraryPlaceholder
        libraryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        libraryRecyclerView.adapter = adapter

        viewModel.fillData()

        viewModel.databaseTracksState.observe(viewLifecycleOwner) { libraryTrackState ->
            render(libraryTrackState)
        }

        viewModel.fillData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }

        return current
    }


    private fun clickOnItem(libraryTrack: FavoriteTrack) {
        Log.d("FavoriteFragment", "Clicked track: $libraryTrack")
        val json = Gson().toJson(libraryTrack)
        Log.d("FavoriteFragment", "Serialized JSON: $json")
        val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra(App.KEY_FOR_PLAYER, json)
        }
        startActivity(intent)
    }

    private fun render(libraryTracksState: FavouriteTrackState) {
        when {
            libraryTracksState.isLoading -> {
                showLoader(true)
                showPlaceHolder(false)
                showContent(false)
            }

            libraryTracksState.libraryTracks.isEmpty() -> {
                showLoader(false)
                showPlaceHolder(true)
                showContent(false)
            }

            else -> {
                showLoader(false)
                showPlaceHolder(false)
                showContent(true)
                adapter?.setTracks(libraryTracksState.libraryTracks)

            }
        }
    }


    private fun showLoader(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
        Log.d(
            "FavouritesFragment",
            if (isVisible) "ProgressBar is visible" else "ProgressBar is hidden"
        )
    }

    private fun showPlaceHolder(isVisible: Boolean) {
        emptyLibraryPlaceholder.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showContent(isVisible: Boolean) {
        libraryRecyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}