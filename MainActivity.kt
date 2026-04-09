package com.example.main
import android.os.Bundle
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.main.data.AppDatabase
import kotlinx.coroutines.launch
import com.example.main.data.User
import com.example.main.data.UserProfile
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.KeyboardType


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Navigation()
        }
    }
}

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login"){
            Login_screen(
                navController = navController,
                onLoginSuccess = { userId ->
                    navController.navigate("home/$userId")
                }
            )
        }
        composable("signup"){
            SignUp_Screen(
                navController = navController,
                onSignupSuccess = { userId ->
                    navController.navigate("info/$userId")
                }
            )
        }
        composable ("home/{userId}"){ backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?:0
            Home_screen(navController, userId)
        }

        composable ("info/{userId}"){backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?:0
            Info_screen(navController, userId)
        }
    }
}

@Composable
fun Login_screen(navController: NavHostController, onLoginSuccess: (Int) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf("") }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val dao = db.userDao()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(0.dp))

        Image(
            painter = painterResource(id = R.drawable.logonew),
            contentDescription = "FitBot Logo",
            modifier = Modifier.size(350.dp)
        )

        Column() {
            Text(
                "Username: ",
                fontSize = 20.sp
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00A8A8),
                    unfocusedBorderColor = Color.Gray
                )
            )
        }

        Spacer(Modifier.height(30.dp))

        Column() {
            Text(
                "Password: ",
                fontSize = 20.sp
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00A8A8),
                    unfocusedBorderColor = Color.Gray
                ),
                visualTransformation = if
                        (passwordVisible) VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            )
        }

        Spacer(Modifier.height(15.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        val user = dao.getUserByUsername(username)

                        if (user != null && user.password == password){
                            onLoginSuccess(user.id)
                        }
                        else{
                            println("Invalid login")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))
            )
            {
                Text("Sign in")
            }
            Button(
                onClick = { navController.navigate("signup") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))
            )
            {
                Text("Sign up")
            }
        }
    }
}

@Composable
fun SignUp_Screen(navController: NavController, onSignupSuccess: (Int) -> Unit){

    var create_user by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var register_password by remember { mutableStateOf("") }
    var confirm_password by remember { mutableStateOf("") }
    var password_error by remember { mutableStateOf("") }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val dao = db.userDao()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logonew),
            contentDescription = "FitBot Logo",
            modifier = Modifier.size(350.dp)
        )

        Column(){
            Text(
                "Create Username: ",
                fontSize = 20.sp
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = create_user,
                onValueChange = { create_user = it },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00A8A8),
                    unfocusedBorderColor = Color.Gray
                )
            )
        }

        Spacer(Modifier.height(30.dp))

        Column() {
            Text(
                "Create Password: ",
                fontSize = 20.sp
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = register_password,
                onValueChange = { register_password = it },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00A8A8),
                    unfocusedBorderColor = Color.Gray
                ),
                visualTransformation = if
                        (passwordVisible) VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            )
        }

        Spacer(Modifier.height(30.dp))

        Column() {
            Text(
                "Confirm Password: ",
                fontSize = 20.sp
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = confirm_password,
                onValueChange = { confirm_password = it },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00A8A8),
                    unfocusedBorderColor = Color.Gray
                ),
                visualTransformation = if
                        (passwordVisible) VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            )
            if (password_error.isNotEmpty()){
                Text(password_error, color = Color.Red)
            }
        }

        Spacer(Modifier.height(15.dp))

        Button(
            onClick = {

                if (create_user.isEmpty() || register_password.isEmpty() || confirm_password.isEmpty()){
                    password_error = "All fields are required"
                }

                else if (register_password.length < 7){
                    password_error = "Password must be at least 7 characters"
                }

                else if(register_password != confirm_password) {
                    password_error = "Passwords do not match"
                }
                else{
                    scope.launch {
                        val existingUser = dao.getUserByUsername(create_user)

                        if (existingUser != null){
                            password_error = "Username already exists"
                        }
                        else{
                            val newUserId = dao.insertUser(
                                User(
                                    username = create_user,
                                    password = register_password
                                )
                            ).toInt()
                            password_error = ""
                            onSignupSuccess(newUserId)
                        }
                    }
                }
        },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))
        )
        {
            Text("Register")
        }

    }
}

@Composable
fun Info_screen(navController: NavController, currentUserId: Int){

    var age by remember { mutableStateOf("Select Age") }
    var expandedAge by remember { mutableStateOf(false) }

    var selectedGender by remember { mutableStateOf("Select Gender") }
    var expandedGender by remember { mutableStateOf(false) }

    var expandedFeet by remember { mutableStateOf(false) }
    var expandedInch by remember { mutableStateOf(false) }

    var feet by remember { mutableStateOf(0) }
    var inch by remember { mutableStateOf(0) }
    val height = (feet * 12) + inch

    var currentWeight by remember { mutableStateOf("") }

    var goalWeight by remember { mutableStateOf("") }

    var gymAccess by remember { mutableStateOf("Select Access") }
    var expandedAccess by remember { mutableStateOf(false) }

    var workoutDays by remember { mutableStateOf("Select Days") }
    var expandedDays by remember { mutableStateOf(false) }

    var diet by remember { mutableStateOf("") }

    var injuries by remember { mutableStateOf("") }

    var activity by remember { mutableStateOf("Select Activity Level") }
    var expandedActivity by remember { mutableStateOf(false) }

    var password_error by remember { mutableStateOf("") }

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val dao = db.userDao()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp)
        .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        Spacer(Modifier.height(20.dp))

        Text(
            "Let's get started!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00A8A8)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Tell us a bit about yourself",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(10.dp))

        Text(
            "Age",
            fontSize = 20.sp
        )

        Spacer(Modifier.height(7.dp))

        Box {
            Button(onClick = { expandedAge = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))) {
                Text(age)
            }

            DropdownMenu(
                expanded = expandedAge,
                onDismissRequest = { expandedAge = false },
                modifier = Modifier.heightIn(max = 250.dp)
            ) {
                (1..99).forEach { number ->
                    DropdownMenuItem(
                        text = { Text(number.toString()) },
                        onClick = {
                            age = number.toString()
                            expandedAge = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text("Gender",
            fontSize = 20.sp)

        Spacer(Modifier.height(7.dp))

        Box {
            Button(onClick = { expandedGender = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))) {
                Text(selectedGender)
            }

            DropdownMenu(
                expanded = expandedGender,
                onDismissRequest = { expandedGender = false },
                modifier = Modifier.heightIn(max = 250.dp)
            ) {
                listOf("Male", "Female").forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            selectedGender = gender
                            expandedGender = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text("Height",
            fontSize = 20.sp)

        Spacer(Modifier.height(7.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            //height in feet
            Box{
                Button(onClick = { expandedFeet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))){
                    Text("${feet} ft")
                }
                DropdownMenu(
                    expanded = expandedFeet,
                    onDismissRequest = { expandedFeet = false },
                    modifier = Modifier.heightIn(max = 250.dp)
                ) {
                    (3..8).forEach { ft ->
                        DropdownMenuItem(
                            text = { Text("$ft ft") },
                            onClick = {
                                feet = ft
                                expandedFeet = false
                            }
                        )
                    }
                }
            }

            //height in inches
            Box{
                Button(onClick = { expandedInch = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))){
                    Text("${inch} in")
                }
                DropdownMenu(
                    expanded = expandedInch,
                    onDismissRequest = { expandedInch = false },
                    modifier = Modifier.heightIn(max = 250.dp)
                ) {
                    (0..11).forEach { inn ->
                        DropdownMenuItem(
                            text = { Text("$inn in") },
                            onClick = {
                                inch = inn
                                expandedInch = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))

        Text(
            "Current weight (in lbs)",
            fontSize = 20.sp
        )

        Spacer(Modifier.height(7.dp))

        OutlinedTextField(
            value = currentWeight,
            onValueChange = {
                if (it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    currentWeight = it
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00A8A8),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(Modifier.height(10.dp))

        Text(
            "Goal weight (in lbs)",
            fontSize = 20.sp
        )

        Spacer(Modifier.height(7.dp))

        OutlinedTextField(
            value = goalWeight,
            onValueChange = {
                if (it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    goalWeight = it
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00A8A8),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(Modifier.height(10.dp))

        Text("Gym Access",
            fontSize = 20.sp)

        Spacer(Modifier.height(7.dp))

        Box {
            Button(onClick = { expandedAccess = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))) {
                Text(gymAccess)
            }

            DropdownMenu(
                expanded = expandedAccess,
                onDismissRequest = { expandedAccess = false },
                modifier = Modifier.heightIn(max = 250.dp)
            ) {
                listOf("At Home", "At Gym").forEach { access ->
                    DropdownMenuItem(
                        text = { Text(access) },
                        onClick = {
                            gymAccess = access
                            expandedAccess = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text("Activity Level",
            fontSize = 20.sp)

        Spacer(Modifier.height(7.dp))

        Box {
            Button(onClick = { expandedActivity = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))) {
                Text(activity)
            }

            DropdownMenu(
                expanded = expandedActivity,
                onDismissRequest = { expandedActivity = false },
                modifier = Modifier.heightIn(max = 250.dp)
            ) {
                listOf("Light", "Moderate", "Intense").forEach { act ->
                    DropdownMenuItem(
                        text = { Text(act) },
                        onClick = {
                            activity = act
                            expandedActivity = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text("How many days a week",
            fontSize = 20.sp)

        Text("do you want to workout?",
            fontSize = 20.sp)

        Spacer(Modifier.height(6.dp))

        Text("Make sure to leave room for rest days!",
            fontSize = 13.sp,
            color = Color.Gray)

        Spacer(Modifier.height(7.dp))

        Box {
            Button(onClick = { expandedDays = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))) {
                Text(workoutDays)
            }

            DropdownMenu(
                expanded = expandedDays,
                onDismissRequest = { expandedDays = false },
                modifier = Modifier.heightIn(max = 250.dp)
            ) {
                (1..7).forEach { days ->
                    DropdownMenuItem(
                        text = { Text(days.toString()) },
                        onClick = {
                            workoutDays = days.toString()
                            expandedDays = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text(
            "List any diets",
            fontSize = 20.sp
        )

        Spacer(Modifier.height(6.dp))

        Text("If none, enter 'N/A'",
            fontSize = 13.sp,
            color = Color.Gray)

        Spacer(Modifier.height(7.dp))

        OutlinedTextField(
            value = diet,
            onValueChange = { diet = it },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00A8A8),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(Modifier.height(10.dp))

        Text(
            "List any injuries",
            fontSize = 20.sp
        )

        Spacer(Modifier.height(6.dp))

        Text("If none, enter 'N/A'",
            fontSize = 13.sp,
            color = Color.Gray)

        Spacer(Modifier.height(7.dp))

        OutlinedTextField(
            value = injuries,
            onValueChange = { injuries = it },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00A8A8),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(Modifier.height(10.dp))

        Text(password_error, color = Color.Red)

        Button(
            onClick = {
                if (age == "Select Age" || selectedGender == "Select Gender" || height == 0
                    || currentWeight == "" || goalWeight == "" || gymAccess == "Select Access"
                    || activity == "Select Activity Level" || workoutDays == "Select Days"
                    || diet == "" || injuries == ""){
                    password_error = "All fields are required!"
                }
                else {
                    scope.launch {
                        dao.insertUserProfile(
                            UserProfile(
                                userId = currentUserId,
                                age = age.toIntOrNull() ?: 0,
                                gender = selectedGender,
                                height = height,
                                weight = currentWeight.toFloatOrNull() ?: 0f,
                                goalWeight = goalWeight.toFloatOrNull() ?: 0f,
                                goalPhysique = "",
                                gymAccess = gymAccess,
                                activityLevel = activity,
                                schedule = workoutDays,
                                diet = diet,
                                injuries = injuries
                            )
                        )
                        navController.navigate("home")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A8A8))
        ) {
            Text("Continue")
        }

    }
}

@Composable
fun Home_screen(navController: NavController, currentUserId: Int){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(0.dp))

        Image(
            painter = painterResource(id = R.drawable.logonew),
            contentDescription = "FitBot Logo",
            modifier = Modifier.size(350.dp)
        )



    }
}