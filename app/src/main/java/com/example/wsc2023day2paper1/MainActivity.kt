package com.example.wsc2023day2paper1


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wsc2023day2paper1.ui.theme.Wsc2023Day2Paper1Theme
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.wsc2023day2paper1.api.BookTickets
import com.example.wsc2023day2paper1.api.ConfirmTickets
import com.example.wsc2023day2paper1.api.GetAirports
import com.example.wsc2023day2paper1.api.GetBookedTickets
import com.example.wsc2023day2paper1.api.GetBookingDetails
import com.example.wsc2023day2paper1.api.GetCountries
import com.example.wsc2023day2paper1.api.GetReturnSchedules
import com.example.wsc2023day2paper1.api.GetSchedules
import com.example.wsc2023day2paper1.api.GetTickets
import com.example.wsc2023day2paper1.api.PostTotalPrice
import com.example.wsc2023day2paper1.models.BookingDetails
import com.example.wsc2023day2paper1.models.ConfirmTicket
import com.example.wsc2023day2paper1.models.Schedule
import com.example.wsc2023day2paper1.models.SearchQuery
import com.example.wsc2023day2paper1.models.Ticket
import com.example.wsc2023day2paper1.models.TicketToConfirm
import com.example.wsc2023day2paper1.models.temSchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.collections.get
import kotlin.div
import kotlin.math.roundToInt
import kotlin.times
import kotlin.toString


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val colorScheme = lightColorScheme(
            primary = Color(0xFFEF3840),
            onPrimary = Color.White,
            // Add other color customizations if needed
        )
        setContent {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = androidx.compose.material3.Typography(),
                content = {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "main_menu"
                    ) {
                        composable("main_menu") { MainMenu(navController) }
                        composable("search_book_flights") { SearchBookFlightsScreen(navController) }
                        composable("select_seats") { SelectSeatsScreen(navController) }
                        composable("view_booking") { ViewBookingScreen(navController) }
                        composable(
                            "acknowledge_booking/{bookingReference}",
                            arguments = listOf(navArgument("bookingReference") { type = NavType.StringType })
                        ) { backStackEntry ->
                            AcknowledgeBookingScreen(
                                navController,
                                backStackEntry.arguments?.getString("bookingReference") ?: ""
                            )
                        }
                    }


                }
            )
        }


    }
}


@Composable
fun MainMenu(navController: NavHostController) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically

            ){
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Icon",
                    modifier = Modifier.size(100.dp)
                        .clickable(onClick = { navController.navigate("main_menu") }),

                )

                Text(
                    text = "World Skills Airlines",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }



            Button(onClick = { navController.navigate("search_book_flights") }) {
                Text("Search and Book Flights")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("select_seats") }) {
                Text("Select Seats")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("view_booking") }) {
                Text("View Booking")
            }
        }
    }
}




fun convertMinutesToHoursAndMinutes(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return "$hours hours and $remainingMinutes minutes"
}
fun isDepartureBeforeArrival(departureDate: String, arrivalDate: String): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return try {
        val depDate = dateFormat.parse(departureDate)
        val arrDate = dateFormat.parse(arrivalDate)
        depDate.before(arrDate)
    } catch (e: Exception) {
        false
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBookFlightsScreen(navController: NavHostController) {
    var departureAirport by remember { mutableStateOf("Juanda International Airport") }
    var arrivalAirport by remember { mutableStateOf("Kaohsiung International Airport") }
    var departureAirports by remember { mutableStateOf(listOf<String>()) }
    var arrivalAirports by remember { mutableStateOf(listOf<String>()) }
    var outboundDate by remember { mutableStateOf("2023-04-08") }
    var returnDate by remember { mutableStateOf("") }
    var cabinType by remember { mutableStateOf("Economy") }
    var adultPassengers by remember { mutableStateOf(1) }
    var childPassengers by remember { mutableStateOf(0) }
    var departureSchedules by remember { mutableStateOf(listOf<Schedule>()) }
    var returnSchedules by remember { mutableStateOf(listOf<Schedule>()) }
    var showBookingDialog by remember { mutableStateOf(false) }
    var selectedDepartureSchedule by remember { mutableStateOf<Schedule?>(null) }
    var selectedArrivalSchedule by remember { mutableStateOf<Schedule?>(null) }
    var showAcknowledgeBookingScreen by remember { mutableStateOf(false) }
    var bookingRef by remember { mutableStateOf("") }

    if (showAcknowledgeBookingScreen) {
        LaunchedEffect(Unit) {
            navController.navigate("acknowledge_booking/$bookingRef")

        }
    }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var airports = GetAirports().getFunction()
            if (airports != null) {
                departureAirports = airports
                arrivalAirports = airports
            }

        }
    }

    if (showBookingDialog) {
        ShowBookingDialog(
            selectedDepartureSchedule!!,
            selectedArrivalSchedule,
            adultPassengers,
            childPassengers,
            navController,
            cabinType,
            onDismiss = { showBookingDialog = false },
            onBookingConfirmed = { bookingReference ->
                if (bookingReference != null) {
                    bookingRef = bookingReference
                    showAcknowledgeBookingScreen = true
                }
            }
        )

    }


    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Row (
                    verticalAlignment = Alignment.CenterVertically

                ){
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "App Icon",
                        modifier = Modifier.size(100.dp)
                            .clickable(onClick = { navController.navigate("main_menu") }),

                        )

                    Text(
                        text = "Search and Book Flights",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            // Departure Airport
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DropDownMenu(
                        items = departureAirports, // Example airport codes
                        name = "Dep. Airport",
                        selectedItem = departureAirport,
                        width = 190,
                        onItemSelected = { departureAirport = it }
                    )
                    DropDownMenu(
                        items = arrivalAirports, // Example airport codes
                        name = "Arrival Airport",
                        selectedItem = arrivalAirport,
                        width = 190,
                        onItemSelected = { arrivalAirport = it }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }


            item {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DatePickerDocked(
                        "outbound_date",
                        "Outbound Date",
                        outboundDate,
                    ) {
                        outboundDate = it
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    // Return Date
                    DatePickerDocked(
                        identifier = "return_date",
                        selectedDate = "Return Date",
                        label = returnDate,
                    ) {
                        returnDate = it
                    }
                }
            }

            item {
                DropDownMenu(
                    items = listOf("Economy", "Business"),
                    name = "Cabin Type",
                    selectedItem = cabinType,
                    width = 400,
                    onItemSelected = { cabinType = it }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = adultPassengers.toString(),
                        onValueChange = { adultPassengers = it.toIntOrNull() ?: 1 },
                        label = { Text("Number of Adult Passengers") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(170.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Number of Child Passengers
                    TextField(
                        value = childPassengers.toString(),
                        onValueChange = { childPassengers = it.toIntOrNull() ?: 0 },
                        label = { Text("Number of Child Passengers") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(170.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            departureAirport = ""
                            arrivalAirport = ""
                            outboundDate = ""
                            returnDate = ""
                            cabinType = "Economy"
                            adultPassengers = 1
                            childPassengers = 0
                            departureSchedules = listOf()
                            returnSchedules = listOf()


                        }
                    ) {
                        Text("Clear")
                    }


                    Button(onClick = {

                        if (  departureAirport == "" || arrivalAirport == "" || outboundDate == "") {
                            Toast.makeText(
                                navController.context,
                                "Please fill in all fields",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        else{
                            if (returnDate.isNullOrEmpty() == false && !isDepartureBeforeArrival(outboundDate, returnDate)) {
                                // Handle the case where the return date is null, empty, or the departure date is not before the arrival date
                                Toast.makeText(
                                    navController.context,
                                    "Departure must be before return",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                            var searchQuery = SearchQuery(
                                from = departureAirport,
                                to = arrivalAirport,
                                departDate = outboundDate,
                                returnDate = returnDate,
                                noOfAdults = adultPassengers,
                                noOfChildren = childPassengers
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                var schedules = GetSchedules().postFunction(searchQuery)
                                if (schedules != null) {
                                    departureSchedules = schedules
                                }
                            }
                            if (returnDate != "") {
                                try {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        var schedules = GetReturnSchedules().postFunction(searchQuery)
                                        if (schedules != null) {
                                            returnSchedules = schedules
                                        }
                                    }
                                } catch (e: Exception) {
                                    returnSchedules = listOf()
                                }

                            }
                        }



                    }) {
                        Text("Search Flights")
                    }

                    Button(
                        onClick = {

                            if(selectedDepartureSchedule != null)
                            {
                                if(returnDate.isNullOrEmpty()== false && selectedArrivalSchedule == null)
                            {
                                Toast.makeText(
                                    navController.context,
                                    "Please select  arrival  schedules",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }
                                showBookingDialog = true
                            }else{
                                Toast.makeText(
                                    navController.context,
                                    "Please select a departure schedule",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    ) {
                        Text("Book Flight")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text("Departure Flights")
            }
            items(departureSchedules) { schedule ->
                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(if (selectedDepartureSchedule == schedule) Color.Red else Color.White),
                        onClick = {
                            selectedDepartureSchedule = schedule
                        }
                    ) {

                        if (schedule.isTransfer == true) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("ScheduleId: ${schedule.id}")
                                Text("Transfer ScheduleId: ${schedule.transferScheduleId}")
                                Text("Date: ${schedule.date}")
                                Text("Departure: ${schedule.departureIata} at ${schedule.departureTime}")
                                Text("Transfer Arrival: ${schedule.arrivalAirIata} at ${schedule.arrivalTime}")
                                Text("Transfer Departure: ${schedule.transferDepartureIata} at ${schedule.transferDepartureTime}")
                                Text("Arrival: ${schedule.transferArrivalIata} at ${schedule.transferArrivalTime}")
                                Text("Duration: ${convertMinutesToHoursAndMinutes(schedule.duration + schedule.transferDuration!!)}")
                                Text(
                                    "Price: \$${
                                        calculateSchedulePrice(
                                            schedule,
                                            cabinType
                                        ).roundToInt()
                                    }"
                                )
                                if (calculateSchedulePrice(schedule, cabinType).roundToInt() == departureSchedules.minOf { calculateSchedulePrice(it, cabinType).roundToInt() }) {
                                    Text(
                                        text = "Lowest Price!",
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        } else {


                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("ScheduleId: ${schedule.id}")
                                Text("Date: ${schedule.date}")
                                Text("Departure: ${schedule.departureIata} at ${schedule.departureTime}")
                                Text("Arrival: ${schedule.arrivalAirIata} at ${schedule.arrivalTime}")
                                Text("Duration: ${convertMinutesToHoursAndMinutes(schedule.duration)}")
                                Text(
                                    "Price: \$${
                                        calculateSchedulePrice(
                                            schedule,
                                            cabinType
                                        ).roundToInt()
                                    }"
                                )
                                if (calculateSchedulePrice(schedule, cabinType).roundToInt() == departureSchedules.minOf { calculateSchedulePrice(it, cabinType).roundToInt() }) {
                                    Text(
                                        text = "Lowest Price!",
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Return Flights")
            }
            items(returnSchedules) { schedule ->
                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(if (selectedArrivalSchedule == schedule) Color.Red else Color.White),
                        onClick = {
                            selectedArrivalSchedule = schedule
                        }
                    ) {
                        if (schedule.isTransfer == true) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("ScheduleId: ${schedule.id}")
                                Text("Transfer ScheduleId: ${schedule.transferScheduleId}")

                                Text("Date: ${schedule.date}")
                                Text("Departure: ${schedule.departureIata} at ${schedule.departureTime}")
                                Text("Transfer Arrival: ${schedule.arrivalAirIata} at ${schedule.arrivalTime}")
                                Text("Transfer Departure: ${schedule.transferDepartureIata} at ${schedule.transferDepartureTime}")
                                Text("Arrival: ${schedule.transferArrivalIata} at ${schedule.transferArrivalTime}")
                                Text("Duration: ${convertMinutesToHoursAndMinutes(schedule.duration + schedule.transferDuration!!)}")
                                Text(
                                    "Price: \$${
                                        calculateSchedulePrice(
                                            schedule,
                                            cabinType
                                        ).roundToInt()
                                    }"
                                )
                                if (calculateSchedulePrice(schedule, cabinType).roundToInt() == returnSchedules.minOf { calculateSchedulePrice(it, cabinType).roundToInt() }) {
                                    Text(
                                        text = "Lowest Price!",
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                            }
                        } else {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Date: ${schedule.date}")
                                Text("Departure: ${schedule.departureIata} at ${schedule.departureTime}")
                                Text("Arrival: ${schedule.arrivalAirIata} at ${schedule.arrivalTime}")
                                Text("Duration: ${convertMinutesToHoursAndMinutes(schedule.duration)}")
                                Text(
                                    "Price: \$${
                                        calculateSchedulePrice(
                                            schedule,
                                            cabinType
                                        ).roundToInt()
                                    }"
                                )
                                if (calculateSchedulePrice(schedule, cabinType).roundToInt() == returnSchedules.minOf { calculateSchedulePrice(it, cabinType).roundToInt() }) {
                                    Text(
                                        text = "Lowest Price!",
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

fun calculatePrice(departureTime: String, normalPrice: Double, flightClass: String): Double {
    val time = LocalTime.parse(departureTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
    val basePrice = when {
        time.isBefore(LocalTime.of(7, 1)) -> normalPrice * 0.6 // Off peak hours
        time.isBefore(LocalTime.of(15, 1)) -> normalPrice // Normal hours
        else -> normalPrice * 1.2 // Peak hours
    }
    val classPrice = when (flightClass) {
        "Economy" -> basePrice
        else -> basePrice * 2.4
    }
    return classPrice
}

fun calculateSchedulePrice(schedule: Schedule, CabinType: String): Double {
    var normalPrice = schedule.price
    val time = LocalTime.parse(schedule.departureTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
    val basePrice = when {
        time.isBefore(LocalTime.of(7, 1)) -> normalPrice * 0.6 // Off peak hours
        time.isBefore(LocalTime.of(15, 1)) -> normalPrice // Normal hours
        else -> normalPrice * 1.2 // Peak hours
    }
    var classPrice = when (CabinType) {
        "Economy" -> basePrice
        else -> basePrice * 2.4
    }
    if(schedule.isTransfer == true)
    {
        classPrice = classPrice * 0.9
        //classPrice += schedule.transferPrice!!.toDouble()
    }
    return classPrice


}

fun calculateScheduleBookingPrice(schedule: Schedule, CabinType: String): Double {
    var normalPrice = schedule.price
    val time = LocalTime.parse(schedule.departureTime, DateTimeFormatter.ofPattern("HH:mm:ss"))
    val basePrice = when {
        time.isBefore(LocalTime.of(7, 1)) -> normalPrice * 0.6 // Off peak hours
        time.isBefore(LocalTime.of(15, 1)) -> normalPrice // Normal hours
        else -> normalPrice * 1.2 // Peak hours
    }
    var classPrice = when (CabinType) {
        "Economy" -> basePrice
        else -> basePrice * 2.4
    }
    if(schedule.isTransfer == true)
    {
        classPrice = classPrice * 0.9
        classPrice += schedule.transferPrice!!.toDouble()
    }
    return classPrice


}


@Composable
fun ShowBookingDialog(
    schedule: Schedule,
    returnSchedule: Schedule?,
    adultPassengers: Int,
    childPassengers: Int,
    navController: NavController,
    cabinType: String,
    onDismiss: () -> Unit,
    onBookingConfirmed: (String) -> Unit
) {
    var totalPrice = ((calculateScheduleBookingPrice(schedule, cabinType)) * adultPassengers +
            calculateScheduleBookingPrice(schedule, cabinType) * 0.7 * childPassengers).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()

    var passengerDetails by remember {
        mutableStateOf(List(adultPassengers + childPassengers) { index ->
            val ticketType = if (index < adultPassengers) "Adult" else "Child"
            Ticket(
                schedule.id,
                cabinType,
                "",
                "",
                "",
                "",
                "",
                "",
                ticketType,
                schedule.isTransfer,
                if (schedule.isTransfer == true) schedule.transferScheduleId else null
            )
        })
    }
    var showDialog by remember { mutableStateOf(true) }
    var countries by remember { mutableStateOf(listOf<String>()) }
    var sendTotalPrice by remember { mutableStateOf(false) }
    var bookingRef by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            var countriesRetrieved = GetCountries().postFunction()
            if (countries != null) {
                countries = countriesRetrieved!!
            }
        }
    }

    if(sendTotalPrice)
    {
        LaunchedEffect(Unit) {
        val postTotalPrice = PostTotalPrice()
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val status = postTotalPrice.postFunction(bookingRef, totalPrice.toDouble())
                if (status) {
                    showDialog = false
                    onBookingConfirmed(bookingRef)
                } else {
//                                    Toast.makeText(
//                                        navController.context,
//                                        "Booking failed",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
                }
            }
        }
        catch (e: Exception) {
            Toast.makeText(
                navController.context,
                "Booking failed",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    }



    if (returnSchedule != null) {
        totalPrice += ((calculateScheduleBookingPrice(returnSchedule, cabinType)) * adultPassengers +
                calculateScheduleBookingPrice(returnSchedule, cabinType) * 0.7 * childPassengers).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()
            },
            title = { Text("Book Flight") },
            text = {
                LazyColumn {
                    item {
                        if (schedule.isTransfer == true) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("ScheduleId: ${schedule.id}")
                                Text("Transfer ScheduleId: ${schedule.transferScheduleId}")

                                Text("Date: ${schedule.date}")
                                Text("Departure: ${schedule.departureIata} at ${schedule.departureTime}")
                                Text("Transfer Arrival: ${schedule.arrivalAirIata} at ${schedule.arrivalTime}")
                                Text("Transfer Departure: ${schedule.transferDepartureIata} at ${schedule.transferDepartureTime}")
                                Text("Arrival: ${schedule.transferArrivalIata} at ${schedule.transferArrivalTime}")
                                Text("Duration: ${convertMinutesToHoursAndMinutes(schedule.duration + schedule.transferDuration!!)}")
                                Text(
                                    "Price: \$${
                                        calculateSchedulePrice(
                                            schedule,
                                            cabinType
                                        ).roundToInt()
                                    }"
                                )
                                Text(
                                    "Transfer Price: \$${
                                        schedule.transferPrice!!.roundToInt()
                                    }"
                                )


                            }
                        } else {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Date: ${schedule.date}")
                                Text("Departure: ${schedule.departureIata} at ${schedule.departureTime}")
                                Text("Arrival: ${schedule.arrivalAirIata} at ${schedule.arrivalTime}")
                                Text("Duration: ${convertMinutesToHoursAndMinutes(schedule.duration)}")
                                Text(
                                    "Price: \$${
                                        calculateSchedulePrice(
                                            schedule,
                                            cabinType
                                        ).roundToInt()
                                    }"
                                )

                            }
                        }
                    }

                    if (returnSchedule != null) {
                        item {
                            if (schedule.isTransfer == true) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("ScheduleId: ${returnSchedule.id}")
                                    Text("Transfer ScheduleId: ${returnSchedule.transferScheduleId}")

                                    Text("Date: ${returnSchedule.date}")
                                    Text("Departure: ${returnSchedule.departureIata} at ${returnSchedule.departureTime}")
                                    Text("Transfer Arrival: ${returnSchedule.arrivalAirIata} at ${returnSchedule.arrivalTime}")
                                    Text("Transfer Departure: ${returnSchedule.transferDepartureIata} at ${returnSchedule.transferDepartureTime}")
                                    Text("Arrival: ${returnSchedule.transferArrivalIata} at ${returnSchedule.transferArrivalTime}")
                                    Text("Duration: ${convertMinutesToHoursAndMinutes(returnSchedule.duration + returnSchedule.transferDuration!!)}")
                                    Text(
                                        "Price: \$${
                                            calculateSchedulePrice(
                                                returnSchedule,
                                                cabinType
                                            ).roundToInt()
                                        }"
                                    )
                                    Text(
                                        "Transfer Price: \$${
                                            returnSchedule.transferPrice!!.roundToInt()
                                        }"
                                    )


                                }
                            } else {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Date: ${returnSchedule.date}")
                                    Text("Departure: ${returnSchedule.departureIata} at ${returnSchedule.departureTime}")
                                    Text("Arrival: ${returnSchedule.arrivalAirIata} at ${returnSchedule.arrivalTime}")
                                    Text("Duration: ${convertMinutesToHoursAndMinutes(returnSchedule.duration)}")
                                    Text(
                                        "Price: \$${
                                            calculateSchedulePrice(
                                                returnSchedule,
                                                cabinType
                                            ).roundToInt()
                                        }"
                                    )

                                }
                            }
                        }
                    }

                    item {
                        Text("Total Price: \$${"%.2f".format(totalPrice)}")                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Enter Passenger Details")
                    }

                    items(passengerDetails.size) { index ->
                        val passengerType = if (index < adultPassengers) "Adult" else "Child"
                        val passengerIndex =
                            if (index < adultPassengers) index + 1 else index - adultPassengers + 1
                        Text("Passenger $passengerType $passengerIndex")
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = passengerDetails[index].firstName,
                            onValueChange = {
                                passengerDetails = passengerDetails.toMutableList()
                                    .apply { this[index] = this[index].copy(firstName = it) }
                            },
                            label = { Text("First Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = passengerDetails[index].lastName,
                            onValueChange = {
                                passengerDetails = passengerDetails.toMutableList()
                                    .apply { this[index] = this[index].copy(lastName = it) }
                            },
                            label = { Text("Last Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = passengerDetails[index].phone,
                            onValueChange = {
                                passengerDetails = passengerDetails.toMutableList()
                                    .apply { this[index] = this[index].copy(phone = it) }
                            },
                            label = { Text("Phone") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = passengerDetails[index].email,
                            onValueChange = {
                                passengerDetails = passengerDetails.toMutableList()
                                    .apply { this[index] = this[index].copy(email = it) }
                            },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = passengerDetails[index].passportNumber,
                            onValueChange = {
                                passengerDetails = passengerDetails.toMutableList()
                                    .apply { this[index] = this[index].copy(passportNumber = it) }
                            },
                            label = { Text("Passport Number") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DropDownMenu(
                            items = countries, // Replace with actual country list
                            name = "Passport Country",
                            selectedItem = passengerDetails[index].passportCountryId,
                            width = 400,
                            onItemSelected = {
                                passengerDetails = passengerDetails.toMutableList().apply {
                                    this[index] = this[index].copy(passportCountryId = it)
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val bookTickets = BookTickets()


                    CoroutineScope(Dispatchers.IO).launch {
                        if (returnSchedule != null) {
                            val bookingReference =
                                bookTickets.postFunction(passengerDetails + passengerDetails.map {
                                    it.copy(scheduleId = returnSchedule?.id ?: 0, transferScheduleId = returnSchedule?.transferScheduleId)                                })
                            if (bookingReference != null) {
                                bookingRef = bookingReference
                                sendTotalPrice = true
                            } else {
//                            showDialog = false
//                            onDismiss()
                                Toast.makeText(
                                    navController.context,
                                    "Booking failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val bookingReference = bookTickets.postFunction(passengerDetails)
                            if (bookingReference != null) {
                                bookingRef = bookingReference
                                sendTotalPrice = true

                            } else {
//                            showDialog = false
//                            onDismiss()
                                Toast.makeText(
                                    navController.context,
                                    "Booking failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                    }



                }) {
                    Text("Confirm Booking")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    onDismiss()
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AcknowledgeBookingScreen(navController: NavController, bookingReference: String) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Booking Confirmed", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your booking reference ID is:")
            Text(bookingReference, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("main_menu") }) {
                Text("Back to Main Menu")
            }
        }
    }
}



@Composable
fun SelectSeatsScreen(navController: NavController) {
    var bookingReference by remember { mutableStateOf("") }
    val businessSeats = 28
    val economySeats = 210
    var getTickets by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var SeatMapSelected by remember { mutableStateOf(mutableMapOf<Int, String>()) }
    var columnHeaders by remember { mutableStateOf(listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")) }
    var rowHeaders by remember { mutableStateOf(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30")) }
    var ticketsToConfirm by remember { mutableStateOf(listOf<TicketToConfirm>()) }
    var bookedTicketList by remember { mutableStateOf(listOf<TicketToConfirm>()) }
    var selectedTicket by remember { mutableStateOf<TicketToConfirm?>(null) }
    var showAcknowledgement by remember { mutableStateOf(false) }
    var displaySeatColor by remember { mutableStateOf(false) }


    if (getTickets) {
        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.IO).launch {
                ticketsToConfirm = listOf()
                val getTicket = GetTickets()
                val TicketList = getTicket.getFunction(bookingReference)
                if (TicketList != null) {
                    ticketsToConfirm = TicketList!!

                }
                else
                {
                    Toast.makeText(navController.context, "Booking Reference ID not found", Toast.LENGTH_SHORT).show()
                }
            }
            CoroutineScope(Dispatchers.IO).launch {
                val getTicket = GetBookedTickets()
                val TicketBookedList = getTicket.getFunction(bookingReference)
                if (TicketBookedList != null) {
                    bookedTicketList = TicketBookedList!!
                } else {

                }
                getTickets = false

            }
        }
    }

    if (showAcknowledgement) {
        AlertDialog(
            onDismissRequest = {
                showAcknowledgement = false
            },
            title = { Text("Booking Acknowledged") },
            text = {
                LazyColumn {
                    item{
                        Text("Booking Reference ID: $bookingReference")
                        Text("Number of Adult Business Tickets Purchased: ${ticketsToConfirm.count { it.cabinTypeId == 2 && it.ticketTypeId == 1}}")
                        Text("Number of Child Business Tickets Purchased: ${ticketsToConfirm.count { it.cabinTypeId == 2 && it.ticketTypeId == 2 }}")
                        Text("Number of Adult Economy Tickets Purchased: ${ticketsToConfirm.count { it.cabinTypeId == 1 && it.ticketTypeId == 1}}")
                        Text("Number of Child Economy Tickets Purchased: ${ticketsToConfirm.count { it.cabinTypeId == 1 && it.ticketTypeId == 2 }}")

                    }
                    items(ticketsToConfirm) { ticket ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    selectedTicket = ticket
                                }
                                .background(if (selectedTicket == ticket) Color.Red else Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Ticket ID: ${ticket.id}")
                                Text("Passenger Name: ${ticket.firstname} ${ticket.lastname}")
                                Text("Passenger Type: ${if (ticket.ticketTypeId == 2) "Child" else "Adult"}")
                                Text("Cabin Type: ${if (ticket.cabinTypeId == 2) "Business" else "Economy"}")
                                Text("Seat: ${ticket.seatNo ?: "Not selected"}")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showAcknowledgement = false
                    navController.navigate("main_menu")
                }) {
                    Text("OK")
                }
            }
        )
    }
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row (
                verticalAlignment = Alignment.CenterVertically

            ){
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Icon",
                    modifier = Modifier.size(100.dp)
                        .clickable(onClick = { navController.navigate("main_menu") }),
                    )

                Text(
                    text = "Select Seats",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        // Display total number of seats
        item {
            Text("Total Business Seats: $businessSeats")
            Text("Total Economy Seats: $economySeats")
            Text("Available Business Seats: ${businessSeats - bookedTicketList.count { it.cabinTypeId == 2 }}")
            Text("Available Economy Seats: ${economySeats - bookedTicketList.count { it.cabinTypeId == 1 }}")
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Booking reference ID field
        item {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                TextField(
                    value = bookingReference,
                    onValueChange = { bookingReference = it },
                    label = { Text("Booking Reference ID") },
                    modifier = Modifier.width(200.dp)
                )
                Button(
                    onClick = {
                        getTickets = true
                        displaySeatColor = true
                    }
                ) {
                    Text("Get")
                }
                Button(
                    onClick = {
                        SeatMapSelected.clear()
                        ticketsToConfirm = ticketsToConfirm.map { ticket ->
                            ticket.copy(seatNo = "Not selected")
                        }
                    }
                ) {
                    Text("Clear")
                }
            }

            LazyRow {
                item {
                    LegendItem(color = Color(0xFFADD8E6), label = "Available Business Seat")
                    LegendItem(color = Color(0xFF0000FF), label = "Booked Business Seat")
                    LegendItem(color = Color(0xFFFFC0CB), label = "Available Economy Seat")
                    LegendItem(color = Color(0xFFFF0000), label = "Booked Economy Seat")
                    LegendItem(color = Color(0xFFA9A9A9), label = "Reserved Seat")
                    LegendItem(color = Color.Black, label = "Not Available")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(ticketsToConfirm) { ticket ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        selectedTicket = ticket
                    }
                    .background(if (selectedTicket == ticket) Color.Red else Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Ticket ID: ${ticket.id}")
                    Text("Passenger Name: ${ticket.firstname} ${ticket.lastname}")
                    Text("Passenger Type: ${if (ticket.ticketTypeId == 2) "Child" else "Adult"}")
                    Text("Cabin Type: ${if (ticket.cabinTypeId == 2) "Business" else "Economy"}")
                    Text("Seat: ${ticket.seatNo ?: "Not selected"}")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        //Column Headers
        item{
            Row {
                Spacer(modifier = Modifier.width(20.dp))
                for (columnIndex in 0 until 6) {
                    Text("${columnHeaders[columnIndex]}")
                    Spacer(modifier = Modifier.width(60.dp))

                }
            }
        }

        // Seat layout business
        items(5) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val columns = 6 // Business seats

                Text("${rowHeaders[rowIndex]}")

                for (columnIndex in 0 until columns) {
                    if (columnIndex == 2 || columnIndex == 4) {
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                    //val seatIndex = rowIndex * columns + columnIndex
                    var boxText = "${columnHeaders[columnIndex]}${rowHeaders[rowIndex]}"
                    var seatsNotAvailable = listOf<String>("E5", "F5")
                    val seatColor = when {
                        displaySeatColor == false -> Color(0xFF90EE90)
                        boxText in seatsNotAvailable -> Color.Black
                        bookedTicketList.any { it.seatNo == boxText } -> Color(0xFFA9A9A9)
                        SeatMapSelected.values.contains<String>(boxText) -> Color(0xFF0000FF)
                        else -> Color(0xFFADD8E6)
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(seatColor)
                            .padding(4.dp)
                            .clickable {
                                if (seatColor == Color.Black)
                                {
                                    Toast.makeText(navController.context, "Seat not available", Toast.LENGTH_SHORT).show()
                                }
                                else if (selectedTicket != null)
                                {
                                    if (selectedTicket!!.ticketTypeId == 2)
                                    {
                                        var ticketId = boxText
                                        var columnAlphabet = ticketId.toString().substring(0, 1)
                                        var columnAlphabetToCheck = ""
                                        var columnIndex = columnHeaders.indexOf(columnAlphabet)
                                        if (columnIndex % 2 ==1)
                                        {
                                            columnAlphabetToCheck = columnHeaders[columnIndex -1]
                                        }
                                        else{
                                            columnAlphabetToCheck = columnHeaders[columnIndex +1]
                                        }

                                        var rowNumber = ticketId.toString().substring(1)
                                        var seatNumberToCheck = "$columnAlphabetToCheck$rowNumber"

                                        if (SeatMapSelected.values.contains<String>(seatNumberToCheck))
                                        {
                                            if(seatColor == Color(0xFF0000FF))
                                            {
                                                var ticketId = selectedTicket!!.id
                                                SeatMapSelected.remove(ticketId)
                                                ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                    if (ticket.id == selectedTicket!!.id) {
                                                        ticket.copy(seatNo = "Not selected")
                                                    } else {
                                                        ticket
                                                    }
                                                }
                                            }
                                            else if (seatColor == Color(0xFFA9A9A9))
                                            {
                                                Toast.makeText(navController.context, "Seat is reserved", Toast.LENGTH_SHORT).show()
                                            }
                                            else {
                                                var boxText =
                                                    "${columnHeaders[columnIndex]}${rowHeaders[rowIndex]}"

                                                var ticketId = selectedTicket!!.id
                                                SeatMapSelected[ticketId] = boxText

                                                ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                    if (ticket.id == selectedTicket!!.id) {
                                                        ticket.copy(seatNo = boxText)
                                                    } else {
                                                        ticket
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(navController.context, "Please select the seat next to the child", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else
                                    {
                                        if(seatColor == Color(0xFF0000FF))
                                        {
                                            var ticketId = selectedTicket!!.id
                                            SeatMapSelected.remove(ticketId)
                                            ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                if (ticket.id == selectedTicket!!.id) {
                                                    ticket.copy(seatNo = "Not selected")
                                                } else {
                                                    ticket
                                                }
                                            }
                                        }
                                        else if (seatColor == Color(0xFFA9A9A9))
                                        {
                                            Toast.makeText(navController.context, "Seat is reserved", Toast.LENGTH_SHORT).show()
                                        }
                                        else {
                                            var boxText =
                                                "${columnHeaders[columnIndex]}${rowHeaders[rowIndex]}"

                                            var ticketId = selectedTicket!!.id
                                            SeatMapSelected[ticketId] = boxText

                                            ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                if (ticket.id == selectedTicket!!.id) {
                                                    ticket.copy(seatNo = boxText)
                                                } else {
                                                    ticket
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                    )
                    {
                        Text("${columnHeaders[columnIndex]}${rowHeaders[rowIndex]}", modifier = Modifier.offset(4.dp, 4.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // column Headers
        item{
            Row {
                Spacer(modifier = Modifier.width(10.dp))
                for (columnIndex in 0 until 10) {
                    Text("${columnHeaders[columnIndex]}")
                    Spacer(modifier = Modifier.width(30.dp))

                }
            }
        }
        items(7) { rowIndex ->
            var indexToAdd = 5
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val columns = 10 // Economy seats

                Text("${rowHeaders[rowIndex+indexToAdd]}")


                for (columnIndex in 0 until columns) {
                    if (columnIndex == 3 || columnIndex == 7) {
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    var boxText = "${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}"
                    val seatColor = when {
                        displaySeatColor == false -> Color(0xFFADD8E6)
                        bookedTicketList.any { it.seatNo == boxText } -> Color(0xFFA9A9A9)
                        SeatMapSelected.values.contains<String>(boxText) -> Color(0xFFFF0000)
                        else -> Color(0xFFFFC0CB)
                    }
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .background(seatColor)
                            .padding(4.dp)
                            .clickable {


                                if (selectedTicket != null)
                            {
                                if (selectedTicket!!.ticketTypeId == 2)
                                {
                                    var ticketId = boxText
                                    var columnAlphabet = ticketId.toString().substring(0, 1)
                                    var columnIndex = columnHeaders.indexOf(columnAlphabet)
                                    var columnAlphabetToCheckList = listOf<String>()
                                    var rowNumber = ticketId.toString().substring(1)

                                    if (columnAlphabet in listOf<String>("B", "E", "F", "I"))
                                    {
                                        columnAlphabetToCheckList += columnHeaders[columnIndex +1] + rowNumber
                                        columnAlphabetToCheckList += columnHeaders[columnIndex -1 ] + rowNumber
                                    }
                                    else if (columnAlphabet in listOf<String>("C",  "G", "J"))
                                    {
                                        columnAlphabetToCheckList += columnHeaders[columnIndex -1 ] + rowNumber
                                    }
                                    else if (columnAlphabet in listOf<String>("A", "D", "H"))
                                    {
                                        columnAlphabetToCheckList += columnHeaders[columnIndex +1] + rowNumber
                                    }



                                    if (SeatMapSelected.values.any { it in columnAlphabetToCheckList })                                    {
                                        if(seatColor == Color(0xFFFF0000))
                                        {
                                            var ticketId = selectedTicket!!.id
                                            SeatMapSelected.remove(ticketId)
                                            ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                if (ticket.id == selectedTicket!!.id) {
                                                    ticket.copy(seatNo = "Not selected")
                                                } else {
                                                    ticket
                                                }
                                            }
                                        }
                                        else if (seatColor == Color(0xFFA9A9A9))
                                        {
                                            Toast.makeText(navController.context, "Seat is reserved", Toast.LENGTH_SHORT).show()
                                        }
                                        else {
                                            var boxText =
                                                "${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}"

                                            var ticketId = selectedTicket!!.id
                                            SeatMapSelected[ticketId] = boxText

                                            ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                if (ticket.id == selectedTicket!!.id) {
                                                    ticket.copy(seatNo = boxText)
                                                } else {
                                                    ticket
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(navController.context, "Please select the seat next to the child", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else
                                {
                                    if(seatColor == Color(0xFFFF0000))
                                    {
                                        var ticketId = selectedTicket!!.id
                                        SeatMapSelected.remove(ticketId)
                                        ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                            if (ticket.id == selectedTicket!!.id) {
                                                ticket.copy(seatNo = "Not selected")
                                            } else {
                                                ticket
                                            }
                                        }
                                    }
                                    else if (seatColor == Color(0xFFA9A9A9))
                                    {
                                        Toast.makeText(navController.context, "Seat is reserved", Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        var boxText =
                                            "${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}"

                                        var ticketId = selectedTicket!!.id
                                        SeatMapSelected[ticketId] = boxText

                                        ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                            if (ticket.id == selectedTicket!!.id) {
                                                ticket.copy(seatNo = boxText)
                                            } else {
                                                ticket
                                            }
                                        }
                                    }
                                }
                            }
                            }
                    )
                    {
                        Text("${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}", Modifier.offset(4.dp, 4.dp), style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(7) { rowIndex ->
            var indexToAdd = 12
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val columns = 10 // Economy seats

                Text("${rowHeaders[rowIndex+indexToAdd]}")


                for (columnIndex in 0 until columns) {
                    if (columnIndex == 3 || columnIndex == 7) {
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    var boxText = "${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}"
                    val seatColor = when {
                        displaySeatColor == false -> Color(0xFFADD8E6)
                        bookedTicketList.any { it.seatNo == boxText } -> Color(0xFFA9A9A9)
                        SeatMapSelected.values.contains<String>(boxText) -> Color(0xFFFF0000)
                        else -> Color(0xFFFFC0CB)
                    }
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .background(seatColor)
                            .padding(4.dp)
                            .clickable {


                                if (selectedTicket != null)
                                {
                                    if (selectedTicket!!.ticketTypeId == 2)
                                    {
                                        var ticketId = boxText
                                        var columnAlphabet = ticketId.toString().substring(0, 1)
                                        var columnIndex = columnHeaders.indexOf(columnAlphabet)
                                        var columnAlphabetToCheckList = listOf<String>()
                                        var rowNumber = ticketId.toString().substring(1)

                                        if (columnAlphabet in listOf<String>("B", "E", "F", "I"))
                                        {
                                            columnAlphabetToCheckList += columnHeaders[columnIndex +1] + rowNumber
                                            columnAlphabetToCheckList += columnHeaders[columnIndex -1 ] + rowNumber
                                        }
                                        else if (columnAlphabet in listOf<String>("C",  "G", "J"))
                                        {
                                            columnAlphabetToCheckList += columnHeaders[columnIndex -1 ] + rowNumber
                                        }
                                        else if (columnAlphabet in listOf<String>("A", "D", "H"))
                                        {
                                            columnAlphabetToCheckList += columnHeaders[columnIndex +1] + rowNumber
                                        }



                                        if (SeatMapSelected.values.any { it in columnAlphabetToCheckList })                                    {
                                            if(seatColor == Color(0xFFFF0000))
                                            {
                                                var ticketId = selectedTicket!!.id
                                                SeatMapSelected.remove(ticketId)
                                                ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                    if (ticket.id == selectedTicket!!.id) {
                                                        ticket.copy(seatNo = "Not selected")
                                                    } else {
                                                        ticket
                                                    }
                                                }
                                            }
                                            else if (seatColor == Color(0xFFA9A9A9))
                                            {
                                                Toast.makeText(navController.context, "Seat is reserved", Toast.LENGTH_SHORT).show()
                                            }
                                            else {
                                                var boxText =
                                                    "${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}"

                                                var ticketId = selectedTicket!!.id
                                                SeatMapSelected[ticketId] = boxText

                                                ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                    if (ticket.id == selectedTicket!!.id) {
                                                        ticket.copy(seatNo = boxText)
                                                    } else {
                                                        ticket
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(navController.context, "Please select the seat next to the child", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else
                                    {
                                        if(seatColor == Color(0xFFFF0000))
                                        {
                                            var ticketId = selectedTicket!!.id
                                            SeatMapSelected.remove(ticketId)
                                            ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                if (ticket.id == selectedTicket!!.id) {
                                                    ticket.copy(seatNo = "Not selected")
                                                } else {
                                                    ticket
                                                }
                                            }
                                        }
                                        else if (seatColor == Color(0xFFA9A9A9))
                                        {
                                            Toast.makeText(navController.context, "Seat is reserved", Toast.LENGTH_SHORT).show()
                                        }
                                        else {
                                            var boxText =
                                                "${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}"

                                            var ticketId = selectedTicket!!.id
                                            SeatMapSelected[ticketId] = boxText

                                            ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                if (ticket.id == selectedTicket!!.id) {
                                                    ticket.copy(seatNo = boxText)
                                                } else {
                                                    ticket
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                    )
                    {
                        Text("${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}", Modifier.offset(4.dp, 4.dp), style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(7) { rowIndex ->
            var indexToAdd = 19
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val columns = 10 // Economy seats

                Text("${rowHeaders[rowIndex+indexToAdd]}")


                for (columnIndex in 0 until columns) {
                    if (columnIndex == 3 || columnIndex == 7) {
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    var boxText = "${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}"
                    val seatColor = when {
                        displaySeatColor == false -> Color(0xFFADD8E6)
                        bookedTicketList.any { it.seatNo == boxText } -> Color(0xFFA9A9A9)
                        SeatMapSelected.values.contains<String>(boxText) -> Color(0xFFFF0000)
                        else -> Color(0xFFFFC0CB)
                    }
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .background(seatColor)
                            .padding(4.dp)
                            .clickable {


                                if (selectedTicket != null)
                                {
                                    if (selectedTicket!!.ticketTypeId == 2)
                                    {
                                        var ticketId = boxText
                                        var columnAlphabet = ticketId.toString().substring(0, 1)
                                        var columnIndex = columnHeaders.indexOf(columnAlphabet)
                                        var columnAlphabetToCheckList = listOf<String>()
                                        var rowNumber = ticketId.toString().substring(1)

                                        if (columnAlphabet in listOf<String>("B", "E", "F", "I"))
                                        {
                                            columnAlphabetToCheckList += columnHeaders[columnIndex +1] + rowNumber
                                            columnAlphabetToCheckList += columnHeaders[columnIndex -1 ] + rowNumber
                                        }
                                        else if (columnAlphabet in listOf<String>("C",  "G", "J"))
                                        {
                                            columnAlphabetToCheckList += columnHeaders[columnIndex -1 ] + rowNumber
                                        }
                                        else if (columnAlphabet in listOf<String>("A", "D", "H"))
                                        {
                                            columnAlphabetToCheckList += columnHeaders[columnIndex +1] + rowNumber
                                        }



                                        if (SeatMapSelected.values.any { it in columnAlphabetToCheckList })                                    {
                                            if(seatColor == Color(0xFFFF0000))
                                            {
                                                var ticketId = selectedTicket!!.id
                                                SeatMapSelected.remove(ticketId)
                                                ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                    if (ticket.id == selectedTicket!!.id) {
                                                        ticket.copy(seatNo = "Not selected")
                                                    } else {
                                                        ticket
                                                    }
                                                }
                                            }
                                            else if (seatColor == Color(0xFFA9A9A9))
                                            {
                                                Toast.makeText(navController.context, "Seat is reserved", Toast.LENGTH_SHORT).show()
                                            }
                                            else {
                                                var boxText =
                                                    "${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}"

                                                var ticketId = selectedTicket!!.id
                                                SeatMapSelected[ticketId] = boxText

                                                ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                    if (ticket.id == selectedTicket!!.id) {
                                                        ticket.copy(seatNo = boxText)
                                                    } else {
                                                        ticket
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(navController.context, "Please select the seat next to the child", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else
                                    {
                                        if(seatColor == Color(0xFFFF0000))
                                        {
                                            var ticketId = selectedTicket!!.id
                                            SeatMapSelected.remove(ticketId)
                                            ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                if (ticket.id == selectedTicket!!.id) {
                                                    ticket.copy(seatNo = "Not selected")
                                                } else {
                                                    ticket
                                                }
                                            }
                                        }
                                        else if (seatColor == Color(0xFFA9A9A9))
                                        {
                                            Toast.makeText(navController.context, "Seat is reserved", Toast.LENGTH_SHORT).show()
                                        }
                                        else {
                                            var boxText =
                                                "${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}"

                                            var ticketId = selectedTicket!!.id
                                            SeatMapSelected[ticketId] = boxText

                                            ticketsToConfirm = ticketsToConfirm.map { ticket ->
                                                if (ticket.id == selectedTicket!!.id) {
                                                    ticket.copy(seatNo = boxText)
                                                } else {
                                                    ticket
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                    )
                    {
                        Text("${columnHeaders[columnIndex]}${rowHeaders[rowIndex+indexToAdd]}", Modifier.offset(4.dp, 4.dp), style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }


        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {


                        val confirmTickets = ConfirmTickets()

                        var ticketsToConfirm = listOf<ConfirmTicket>()

                        if (SeatMapSelected.isEmpty()) {
                            Toast.makeText(navController.context, "Please select a seat", Toast.LENGTH_SHORT).show()
                            return@Button
                        }


                        for (ticket in SeatMapSelected) {
                            var rowNumber = ticket.value.toString().substring(1).toInt()
                            if (rowNumber >= 6 && selectedTicket!!.cabinTypeId == 1) {
                                Toast.makeText(navController.context, "This is a business seat. Please select an economy seat.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            if (rowNumber < 6 && selectedTicket!!.cabinTypeId == 2) {
                                Toast.makeText(navController.context, "This is a economy seat. Please select an business seat.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            val newTicket = ConfirmTicket(ticket.key, ticket.value)
                            ticketsToConfirm += newTicket
                        }

                        try {
                            CoroutineScope(Dispatchers.IO).launch {
                                val isSuccess = confirmTickets.postFunction(ticketsToConfirm)

                                if (isSuccess) {
                                    showAcknowledgement = true
                                } else {
                                    Toast.makeText(navController.context, "Booking failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            Toast.makeText(navController.context, "Booking failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Confirm Booking")
                }
                Button(
                    onClick = {
                        navController.navigate("main_menu")
                    }
                ) {
                    Text("Back to Main Menu")
                }
            }

        }

        item {
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red)
            }
        }
    }
}


@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color)
        )
        Text(label)
    }
}

@Composable
fun ViewBookingScreen(navController: NavController) {
    var bookingReference by remember { mutableStateOf("") }
    var bookingDetails by remember { mutableStateOf<BookingDetails?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically

            ){
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Icon",
                    modifier = Modifier.size(100.dp)
                        .clickable(onClick = { navController.navigate("main_menu") }),

                    )

                Text(
                    text = "Search Booking Reference",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }


            TextField(
                value = bookingReference,
                onValueChange = { bookingReference = it },
                label = { Text("Booking Reference ID") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                isLoading = true
                CoroutineScope(Dispatchers.IO).launch {
                    val booking = GetBookingDetails().getFunction(bookingReference)
                    if (booking != null) {
                        bookingDetails = booking
                        errorMessage = ""
                    } else {
                        errorMessage = "Booking Reference ID not found"
                    }
                    isLoading = false
                }
            }) {
                Text("Get Booking Details")
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red)
            } else if (bookingDetails != null) {
                Section1(bookingDetails!!)
                Section2(bookingDetails!!)
                Section3(bookingDetails!!)
            }
        }
    }
}

@Composable
fun Section1(bookingDetails: BookingDetails) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Booking Reference: ${bookingDetails.bookingReference}")
        Text("Name: ${bookingDetails.ticket[0].firstname} ${bookingDetails.ticket[0].lastname}")
        Text("Phone: ${bookingDetails.ticket[0].phone}")
        Text("Email: ${bookingDetails.ticket[0].email}")
        Text("Total Cost: $${bookingDetails.totalCost}")
    }
}
@Composable
fun Section2(bookingDetails: BookingDetails) {

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        items(bookingDetails.schedule) { schedul ->
            Text("You Will Leave ${schedul.departureIata} pm ${schedul.date} at ${schedul.departureTime}")
        }

        item {
            Text("You will spend a total of ${"%.2f".format(calculateTotalHours(bookingDetails.schedule))} hours in the air")        }
    }

}
fun calculateTotalHours(schedules: List<temSchedule>): Double {
    return schedules.sumOf { it.travelingTime.toDouble() / 60.0 }

}


@Composable
fun Section3(bookingDetails: BookingDetails) {
    val businessFullness = bookingDetails.businessCapacity
    val economyFullness =  bookingDetails.economyCapacity

    var firstScheduleId = remember{mutableStateOf(bookingDetails.schedule.first().id)}

    val businessStatus = when {
        businessFullness >= 80 -> "very full"
        businessFullness > 40 -> "not very full"
        else -> "quite empty"
    }

    val economyStatus = when {
        economyFullness >= 80 -> "very full"
        economyFullness > 40 -> "not very full"
        else -> "quite empty"
    }



    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Text("Total Adults: ${bookingDetails.ticket.count { it.ticketTypeId == 1 && it.scheduleId == firstScheduleId.value }}")
            Text("Total Children: ${bookingDetails.ticket.count { it.ticketTypeId == 2 && it.scheduleId == firstScheduleId.value}}")
            Text("Total Business Seats: ${bookingDetails.ticket.count { it.cabinTypeId == 2 && it.scheduleId == firstScheduleId.value}}")
            Text("Total Economy Seats: ${bookingDetails.ticket.count { it.cabinTypeId == 1 && it.scheduleId == firstScheduleId.value}}")
        }
        item {
            val mainPassenger = bookingDetails.ticket.first()
            Text("Main Passenger: ${mainPassenger.firstname}, Seat: ${mainPassenger.seatNo}")
        }
        items(bookingDetails.ticket.drop(1).filter { it.scheduleId == bookingDetails.schedule.first().id }) { passenger ->
            Column {
                Text("Passenger: ${passenger.firstname}")
                Text("Passport Number: ${passenger.passportNumber}")
                Text("Passport Country: ${passenger.passportCountryId}")
                Text("Seat: ${passenger.seatNo}")
            }
        }
        item{
            Text("The flight is $businessStatus in the business class and $economyStatus in the economy class")
            Text("The Business seats are (${businessFullness}% Booked and)")
            Text("The Economy seats are  (${economyFullness}% booked on this flight)")        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(
    identifier: String,
    selectedDate: String,
    label: String,
    onDateSelected: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    var selectedDateText by remember { mutableStateOf(selectedDate) }

    Box(
        modifier = Modifier
            .width(200.dp)
            .padding(5.dp)
    ) {
        OutlinedTextField(
            value = selectedDateText,
            onValueChange = { },
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .width(200.dp)
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart,
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val selectedDateMillis = datePickerState.selectedDateMillis
                                    if (selectedDateMillis != null) {
                                        onDateSelected(convertMillisToDate(selectedDateMillis))
                                    }
                                    showDatePicker = false
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(
    items: List<String>,
    name: String,
    selectedItem: String,
    width: Int,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .width(width.dp)
            .padding(5.dp) // Adjust the width as needed
    ) {
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            label = { Text(name) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        expanded = false
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

