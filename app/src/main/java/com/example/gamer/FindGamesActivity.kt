package com.example.gamer

//import com.google.android.material.navigation.NavigationView
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yuyakaido.android.cardstackview.*

class FindGamesActivity : AppCompatActivity(), CardStackListener {

    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val cardStackView by lazy { findViewById<CardStackView>(R.id.card_stack_view) }
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private var adapter: CardStackAdapter? = null
    private var userLatitude: Double = 37.7601
    private var userLongitude: Double = -89.1
    private lateinit var mDatabase: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findgames)

        //set up the basic references
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mDatabase = FirebaseDatabase.getInstance().reference

        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        setLocation(location)
                    } else {
                        setCoordinates()
                    }

                }.addOnFailureListener {
                    Log.d("PleaseDoSomething", "WhyAreYoutNotWorking:")

                    setCoordinates()

                    val alertDialog = AlertDialog.Builder(this@FindGamesActivity).create()
                    alertDialog.setTitle("Current Location could not be found.")
                    alertDialog.setMessage("Your current location could not be found, we will be displaying all games, no matter its location.")
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK"
                    ) { dialog, which -> dialog.dismiss() }
                    alertDialog.show()
                }

        print("A")
        FirebaseAuth.getInstance().currentUser!!.email?.let { getObjects(it) }
        //setupNavigation()


    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
        finish()
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")

        /*
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }
         */

        if (direction == Direction.Left) {
            Log.d("CardStackView", "Left CardWasSwipedPlsHelp: p = $adapter.getSpots()[manager.topPosition].url}")
            getGame(adapter!!.getSpots()[manager.topPosition - 1].key, false)
        } else if (direction == Direction.Right) {
            //Do the other thing
            Log.d("RightSWIPE", "Right CardWasSwipedPlsHelp: p = ${adapter!!.getSpots()[manager.topPosition - 1].url}")
            getGame(adapter!!.getSpots()[manager.topPosition - 1].key, true)
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }

    private fun setupCardStackView() {

        initialize()
    }

    private fun setupButton() {
        val skip = findViewById<View>(R.id.skip_button)
        skip.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
        /*

        val rewind = findViewById<View>(R.id.rewind_button)
        rewind.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                    .setDirection(Direction.Bottom)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(DecelerateInterpolator())
                    .build()
            manager.setRewindAnimationSetting(setting)
            cardStackView.rewind()
        }

         */

        val like = findViewById<View>(R.id.like_button)
        like.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
        val textView: TextView = findViewById(R.id.textView3)
        textView.visibility = View.VISIBLE
    }
    /*
    private fun paginate() {
        val old = adapter.getSpots()
        val new = old.plus(createSpots())
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }
     */
    /*
    private fun reload() {
        val old = adapter.getSpots()
        val new = createSpots()
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }
     */
    /*
    private fun addFirst(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                add(manager.topPosition, createSpot())
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }
     */
    /*
    private fun addLast(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            addAll(List(size) { createSpot() })
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }
     */
    /*
    private fun removeFirst(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(manager.topPosition)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

     */
    /*
    private fun removeLast(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(this.size - 1)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

     */
    /*
    private fun replace() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, createSpot())
        }
        adapter.setSpots(new)
        adapter.notifyItemChanged(manager.topPosition)
    }

    private fun swap() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            val first = removeAt(manager.topPosition)
            val last = removeAt(this.size - 1)
            add(manager.topPosition, last)
            add(first)
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

     */
    /*
    private fun createSpot(): Spot {
        return Spot(
                name = "Yasaka Shrine",
                city = "Kyoto",
                url = "https://source.unsplash.com/Xq1ntWruZQI/600x800",
        )
    }

     */

    private fun createSpots(spots: List<Spot>) {
        adapter = CardStackAdapter(spots)

        setupCardStackView()
        setupButton()
    }

    private fun getObjects(user: String) {
        // Most viewed posts

        val spots = ArrayList<Spot>()

        val myMostViewedPostsQuery = mDatabase.child("games").child("games")

        myMostViewedPostsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    // TODO: handle the post
                    Log.d("LookingForSomething", "WhyIsItNull")
                    val game = postSnapshot.getValue(Game::class.java)
                    if (game != null) {
                         if (game.owner != user) {
                            if(checkUser(user, game) && DistanceCalculator.threshold(LatLng(game.userLatitude, game.userLongitude), LatLng(userLatitude, userLongitude))) {
                                spots.add(Spot(name = game.name, city = game.bio, key = game.key, latitude = game.userLatitude, longitude = game.userLongitude, url = game.url))
                            }
                         }

                    }
                }
                createSpots(spots)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }

    private fun checkUser(user: String, game: Game): Boolean {
        if (game.users == null) {
            return true
        }
        for (keys in game.users) {
            val key = keys.key
            if (user == game.users.get(key)!!.user) {
                return false
            } else if (user == game.owner) {
                return false
            }
        }
        return true
    }

    private fun getGame(key: String, swipe: Boolean) {
        //var game: Game? = null

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val game: Game = dataSnapshot.getValue(Game::class.java)!!
                // ...
                writeUserGame(FirebaseAuth.getInstance().currentUser!!.email!!, game, swipe)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("GG", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        mDatabase.child("games").child("games").child(key).addListenerForSingleValueEvent(postListener)
    }

    private fun writeUserGame(user: String, game: Game, swipe: Boolean) {
        var thisUser: Users? = null

        if (swipe) {
            thisUser = Users(user, PlayerState.rightSwipe)
        } else {
            thisUser = Users(user, PlayerState.notComing)
        }

        val myKey = mDatabase.child("games").child("games").child(game.key).child("users").push().key
        mDatabase.child("games").child("games").child(game.key).child("users").child(myKey!!).setValue(thisUser)
    }

    private fun setLocation(location: Location) {
        userLatitude = location.latitude
        userLongitude = location.longitude
    }

    private fun setCoordinates() {
        userLatitude = 0.0
        userLongitude = 0.0

        ProximityThresholdActivity.setProximityThreshold(40075000)
    }

}