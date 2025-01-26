package com.generator.wildfyre.activity
import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.generator.wildfyre.api.GoogleSheetApi
import com.generator.wildfyre.data.CalendarData
import com.generator.wildfyre.defaultSettings
import com.generator.wildfyre.dialog.CloseDialog
import com.generator.wildfyre.dialog.UrlCheckerDialog
import com.generator.wildfyre.enum.WebOpenerDB
import com.generator.wildfyre.events.AddUrlEvent
import com.generator.wildfyre.events.IdleCheckerEvent
import com.generator.wildfyre.local_db.DatabaseHandler
import com.generator.wildfyre.model.GoogleSheet
import com.generator.wildfyre.model.URLData
import com.generator.wildfyre.presenter.GoogleSheetPresenterClass
import com.generator.wildfyre.presenter.GoogleSheetView
import com.generator.wildfyre.services.IdleChecker
import com.generator.wildfyre.R
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_action_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode



class MainActivity : AppCompatActivity(), GoogleSheetView, AdapterView.OnItemSelectedListener {
    var db = DatabaseHandler(this)
    var urlData: MutableList<URLData.Details> = ArrayList()
    var total = 0
    var closeDialog = CloseDialog()
    lateinit var urlCheckerDialog: UrlCheckerDialog
    var googleSheet : GoogleSheet.Result? = null
    val BASE_URL = "https://sheets.googleapis.com/"
    var sheetName = arrayListOf<String>("By Day","By Date","By Time")
    var selectedSheetIndex : Int =  0

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val apiServer by lazy {
        GoogleSheetApi.create(this)
    }
    val presenter = GoogleSheetPresenterClass(this, apiServer)

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        urlCheckerDialog = UrlCheckerDialog(this)
        try {
            supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
            supportActionBar?.setCustomView(R.layout.custom_action_bar)
            action_bar_title.setText("WILDFYRE Generator")
            action_bar_subtitle.setText("v.20250126.1")
                //wildfyre_v.20250126.1
                //WildFyre black to WildFyre
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        bind()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onResume() {
        super.onResume()
        start.setImageResource(R.drawable.round_play_button)
        startService(Intent(this,IdleChecker::class.java))
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
        stopService(Intent(this,IdleChecker::class.java))
    }

    private fun bind() {
        getSheetName()
        setClickEvent()
    }

    private fun setClickEvent() {
        addURL.setOnClickListener {
            addURLEditText(null)
            if (total == 25) {
                addURL.isEnabled = false
            }
        }

        start.setOnClickListener {
            start.setImageResource(R.drawable.pause)
            checkDataBase()
        }

        shuffleBtn.setOnClickListener {
            shuffleUrl()
        }

        arrangeBtn.setOnClickListener {
            var tempData = urlData.filter { item ->
                item.url != ""
            }.sortedBy { item -> item.url }
            removeUrlView()
            tempData.forEach { item ->
                addURLEditText(item.url)
            }
        }

        resetBtn.setOnClickListener {
            removeUrlView()
            defaultSettings?.url?.forEach { item ->
                addURLEditText(item)
            }
        }
    }

    private fun saveFactor() {
        db.deleteDatabase(WebOpenerDB.TABLE_FACTOR.getValue(), {
            db.insertFactor(factor.text.toString())
        })
    }

    private fun saveUrl() {
        db.deleteDatabase(WebOpenerDB.TABLE_URL.getValue(),{
            urlData.shuffle()
            urlData.forEach { item ->
                db.insertURL(item)
            }
        })

    }

    private fun shuffleUrl() {
        var shuffledUrl = urlData.shuffled()
            .filter { data ->
                data.url != ""
            }
        removeUrlView()

        shuffledUrl
        shuffledUrl.forEach { item ->
            addURLEditText(item.url)
        }
    }

    private fun removeUrlView() {
        urlData.clear()
        urlCon.removeAllViews()
    }

    private fun checkDataBase() {
        checkURL()
    }

    private fun checkURL() {
        var sheet = processSheetName()
        db.insertSheetSettings(sheetName[selectedSheetIndex]) // save the sheet settings
        presenter.getUrl("${BASE_URL}v4/spreadsheets/${googleSheetID.text}/values/${sheet}?dateTimeRenderOption=FORMATTED_STRING&majorDimension=ROWS&valueRenderOption=FORMATTED_VALUE&key=AIzaSyAdETbAw9fqHW5wCv5Hnipoc1kvGmCEfoA")
    }

    private fun getSheetName() {

        presenter.getSheet("${BASE_URL}v4/spreadsheets/${googleSheetID.text}?key=AIzaSyAdETbAw9fqHW5wCv5Hnipoc1kvGmCEfoA")
    }

    private fun processSheetSetting() {
        var data = db.getSheetSettings()

        if (data != null) {
            //set the default value of spinner sheet
            sheetName.forEachIndexed { index, element ->
                if (element == data.sheet_name) {
                    googleSheetNameSpinner.setSelection(index)
                }
            }
        }
    }

    private fun processSheetName() : String {
        val sheet = sheetName[selectedSheetIndex]
        return when (sheet) {
            "By Day" -> CalendarData().processByDay()
            "By Date" -> CalendarData().processByDate()
            "By Time" -> CalendarData().processByTime()
            else -> sheet
        }
    }

    private fun createDropdownSheet() {
        var arrayAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, sheetName)
        googleSheetNameSpinner.adapter = arrayAdapter
        googleSheetNameSpinner.onItemSelectedListener = this
        processSheetSetting()
    }

    private fun addURLEditText(url: String?, pages:String = "") {
        addURL.text = "add URL (${++total})"
        var editText = EditText(this)
        var horizontalCon = LinearLayout(this)
        var removeBtn = ImageView(this)
        var layout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        editText.layoutParams = layout
        editText.id = View.generateViewId()
        editText.hint = "Enter URL"
        editText.setText(url)
        var data = URLData.Details(
            url,
            editText.id.toString(),
            pages
        )
        urlData.add(data)
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkArray(editText.id.toString(), s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        horizontalCon.layoutParams = layout
        horizontalCon.orientation = LinearLayout.HORIZONTAL
        horizontalCon.id = View.generateViewId()

        var imageLayout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        imageLayout.setMargins(0,20,10,-10)

        removeBtn.layoutParams = imageLayout
        removeBtn.setImageResource(R.drawable.ic_baseline_delete_24)
        removeBtn.setPadding(0,0,0,0)

        removeBtn.setOnClickListener {
            var tempData = urlData.filter { item ->
                item != data
            }
            removeUrlView()
            tempData.forEach { item ->
                addURLEditText(item.url)
            }
        }

        horizontalCon.addView(removeBtn)
        horizontalCon.addView(editText)


        urlCon.addView(horizontalCon)
    }

    private fun checkArray(id: String, url: String) {
        var index = -1
        urlData.forEach { data ->
            index++
            if (id == data.id) {
                var newData = URLData.Details(url, id,"")
                urlData[index] = newData
            }
        }
    }

    override fun onBackPressed() {
        closeDialog.showDialog(this)
    }

    override fun responseGetUrl(data: GoogleSheet.Result) {
       data.values.removeAt(0)

       googleSheet = data
       data.values.forEach { item ->
           addURLEditText(item[0],item[1])
       }
        saveUrl()
        saveFactor()
        stopService(Intent(this,IdleChecker::class.java))
        startActivity(Intent(this, WordpressLoaderActivity::class.java))
    }

    override fun responseGetUrlFailed(data: String) {

    }

    override fun responseGetSheet(data: GoogleSheet.SheetResult) {
        // filter the sheet name and store in array for spinner
        data.sheets.forEach { item ->
            sheetName.add(item.properties.title)
        }
        createDropdownSheet()
    }

    override fun responseGetSheetFailed(data: String) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedSheetIndex = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onAddUrlEvent(event: AddUrlEvent) {
        addURLEditText(event.url)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onIdleCheckerEvent(event: IdleCheckerEvent) {
        start.setImageResource(R.drawable.pause)
        checkDataBase()
    }
}