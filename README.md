Hello! We are On-Care from Singapore Nanyang Technological University.🧑‍🎓💕

On-Care Application is a mobile health application specially tailored for middle-aged adults, with a focus on those dealing with diabetes and high blood pressure.

The primary goal is to bridge the existing gap in the market by offering a comprehensive solution that emphasizes preventive and precautionary measures in health management.

We have utilised state-of-the-art software and tools such as Android Studio, Firebase, and DialogFlow CX for this project.

# Project Description:

### **_Splash Screen_**
Upon launching the application on the Android phone, users will be greeted with an animated splash screen displaying the app's logo and slogan. This introductory screen enhances the user experience by providing a visually engaging way to start the app and sets the tone for the user's interaction with the rest of the interface.

### **_Page Slider Activity_**
Following the splash screen, users can embark on a brief guided tour of the application's features, enabling them to gain a comprehensive understanding of its functionalities.

### **_Login Activity_**
Users will then be directed to the login screen where they will be prompted to enter the email and password they used during registration. 
If the user does not yet have an account, they can easily register by clicking on the link below the 'Sign In Now' button. 
If a user attempts to log in without entering the necessary credentials, they will receive a prompt indicating the need to fill in the required fields to proceed.

Additionally, if a user attempts to access the app without registering an account, an error message will be displayed. 
This ensures that only registered users can access the app's features and enhances security by preventing unauthorized access attempts. 
Moreover, a ‘Forget Password’ feature is available for users to retrieve their accounts without the need to create a new one. 
By simply entering their email address, users can receive a link to reset their password via email.

### **_Registration Activity_**
First-time users are required to complete a quick registration process by creating a new account in order to have access to the application. 
During registration, users are prompted to enter their full name, email address, contact, date of birth, gender, and password.
Users also have the option to upload a profile photo by selecting an image from their phone gallery. 
After completing the registration process, the user's information is securely stored in Firebase. 
The user can then easily log in at any time using the same email and password they used during registration. 
With this simple and secure process, users can start using the application and enjoy all its features without any hassle. 

If registered users mistakenly land on the registration page, they could simply navigate back to the login page by clicking on the link located below the ‘Register Now’ button, instead of restarting the application.

### **_Main Activity_**
After the user login or register an account, they will be directed to the Main-Page activity. 
On-Care Application offers users a choice of eight features: _Journal, Food, Medical Record, Appointment, News, Health, Chatbot, and Profile._ 

Additionally, there is a logout button in the top-right corner. 
If a user accidentally presses the logout button, a confirmation prompt will appear to ensure they genuinely intend to log out of the application. 
This safeguard helps prevent unintended disruptions to the user experience. 

The application’s user interface is designed with ease of use and accessibility in mind, featuring a clean and intuitive layout that allows users to navigate effortlessly between the eight features. 
Furthermore, each feature is represented by an identifiable icon and label, making it easy for users to recognise and select the desired option. 

### **_Health Dashboard_**
The Health Dashboard feature provides users with an overview of their health risk levels regarding BMI, blood pressure, and diabetes. 
First-time users who have yet to utilise the Journal features will see a plain view of the Health Dashboard.
This is because the data displayed on the Dashboard is sourced from the Journal section.

As users begin to engage with the Journal features and input their health information, the Dashboard will reflect their personalised risk levels for each category using colour-coded indicators.

#### The colour representation for each type of BMI is presented below:
Severe Thinness - Red

Moderate Thinness - Yellow

Mild Thinness - Yellow

Normal - Green

Overweight - Yellow

Obese Class I - Yellow

Obese Class II – Red
 
#### The colour representation for each type of Blood Pressure is presented below:
Low Blood pressure – Yellow

Normal Blood pressure – Green

High Blood Pressure - Red

#### The colour representation for each type of Diabetes is presented below:
Low Diabetes – Yellow

Normal Diabetes – Green

High Diabetes – Red

### **_Journal Activity_**
Within the Journal section, users can access four key features: Blood Pressure, Glucose Level, BMI, and FAQS.
Each of these features caters to specific health monitoring and information needs, providing users with valuable tools to track and analyse crucial health parameters. 

### **_Blood Pressure Activity_**
In the Blood Pressure activity, users are prompted to input comprehensive information about their blood pressure readings, including the date and time of the measurement, as well as the systolic and diastolic values.
Users are not allowed to input records for future dates to ensure the users maintain a meticulous and precise log of their blood pressure readings, eliminating any confusion or inaccuracies that could arise when recording measurements.

While it’s ideal for users to own a blood pressure monitor at home, there’s no need to worry if they don’t. Numerous healthcare centres offer blood pressure monitoring services. Additionally, users can visit their local community centres, which often have health stations equipped with blood pressure monitors available for residents to use free of charge.

Upon pressing the “Result” button, users are presented with a message indicating whether they have hypotension, normal blood pressure, or hypertension. 
Additionally, a table and a line graph are displayed, providing a detailed overview of their blood pressure readings. 
The visual representation of their blood pressure data also helps users to identify trends or patterns, further supporting informed decision-making regarding their health and well-being. 

The Blood Pressure Diary empowers users to track their blood pressure readings consistently and accurately, promoting comprehensive monitoring that leads to improved health outcomes and an elevated quality of life. 
By offering the capability to review past entries, users can identify trends or patterns in their blood pressure levels, enabling them to make informed decisions about their health. 

### **_Diabetes Activity_**
Similar to the Blood Pressure activity, the Diabetes activity requires users to input detailed information about their diabetes readings.
This includes the date and time of the measurement, their glucose value, as well as their diabetes status using the dropdown list. 
Users also need to indicate when they are taking the reading using another dropdown list, with options such as: “Before breakfast”, “2 hours after breakfast”, “Before lunch, 2 hours after lunch”, “Before dinner”, “2 hours after dinner”, and “Before bedtime”.

The rationale behind taking glucose readings two hours after consuming a meal is to ensure accurate readings. 
Measuring glucose levels immediately after eating can result in a spike in the reading, which may not accurately reflect the user’s typical blood sugar levels. 
By tracking and monitoring their glucose levels consistently, users can better manage their diabetes and make informed decisions about their health and well-being.

Singaporeans have several options for measuring their glucose levels. 
They can use home glucose meters, which can be purchased from pharmacies or online stores, allowing them to monitor their levels conveniently. 
Alternatively, they can visit healthcare centres which offer professional glucose testing services.

Once users have entered the required information and pressed the “Result” button, a message will be displayed below. 
If the user has a low glucose level, the message will include additional notes with directions on how to increase their glucose level.

This personalised feedback can be crucial in helping users to take appropriate actions to manage their diabetes more effectively.

Similar to the Blood Pressure activity, there is also a table displayed for Diabetes where data is retrieved from Cloud Firestore.
This table provides a detailed overview of the user’s glucose readings, making it easy to track and monitor their glucose levels over time. 

### **_BMI Activity_**
Another feature under the Journal section is the BMI calculator. 
To use this feature, users are required to enter all necessary details before they can proceed to the next activity. 
This precaution prevents the app from crashing due to incomplete information. 

Users can select their gender, adjust their height and weight using scrollbars, and input their age.
By providing these details, the BMI calculator can accurately compute the user’s Body Mass Index, offering valuable insights into their overall health and fitness levels. 

Upon entering the required information, users will be redirected to the next activity where their BMI readings are calculated and displayed.
There are seven categories to classify the BMI results: _severe thinness, moderate thinness, mild thinness, normal, overweight, obese class I, and obese class II._
These categories help users understand their current health status concerning their weight, providing insights into areas for improvement or maintenance.

### **_FAQs Activity_**
The FAQs section presents a list of commonly asked questions, allowing users to effortlessly find the information they are looking for and enhance their understanding of various health topics. 
This feature empowers users to make well-informed decisions about their well-being by providing easily accessible answers to their questions.

If a user finds a question relevant to their query, they can simply press on the question, and the answer will be displayed in a dropdown list.
This user-friendly feature allows for quick and easy access to the answer they seek, further enhancing their overall experience with the application.

However, if the FAQs do not adequately address a user’s query, they have the option to engage with the chatbot by clicking on the underlined word. This action will redirect them to the Chatbot activity, where they can participate in a more interactive and personalised conversation to obtain the information they need.

### **_Reminder_**
On-Care incorporate a built-in reminder system that sends notifications to users every 24 hours, encouraging them to take their blood pressure and diabetes readings consistently. 
This daily reminder assists users in maintaining a routine, enabling them to effectively monitor their health and identify trends or changes over time. 
By actively tracking these essential health parameters, users can better manage their conditions.

### **_Food Activity_**
In the Food activity, users can access a customised list of foods to eat and avoid, specifically tailored to their health conditions.
This personalised list is generated based on the user’s input from the Journal section, taking into account their blood pressure, glucose levels, and BMI status. 
By retrieving and consolidating relevant information from the database, the Food activity serves as a valuable resource, guiding users in making healthier food choices that cater to their health needs and promoting overall wellbeing. 

In addition to the personalised food recommendations, the Food activity also provides users with easy and delicious recipes to try at home. 
These recipes are designed to offer more variety and inspire users to experiment with different healthy dishes. 
By introducing new and tasty meal options, the app aims to encourage users to maintain a balanced diet and stay motivated on their journey towards better health.

By simply tapping the recipe image, users can access the activity that provides detailed information on the preparation of the selected dish.
This includes a comprehensive list of ingredients and step-by-step instructions to guide users in whipping up a delicious and healthy meal.
This feature makes it easy for users to try new recipes and expand their culinary repertoire, encouraging them to maintain a nutritious and diverse diet. 

The “Food to Avoid” section, tailored to the user’s inputs in the Journal, offers personalised recommendations on which foods to steer clear of based on their specific health conditions.
By considering factors such as blood pressure, glucose levels, and BMI status, this feature provides valuable insights into the foods that may negatively impact user’s well-being. 
This helps users make more informed dietary choices and maintain and healthy lifestyle that aligns with their individual needs.

### **_Medical Record Activity_**
The Medical Record feature provides users with a secure and organised way to store and manage their personal records. Users have the flexibility to either upload photos of their medical reports or input the information manually. They can also conveniently view their records within the app.

When users chose to upload photo of their medical record, they simply need to tap on the plus sign. 
This action directs them to their device’s gallery, where they can select the photo of their medical record.

If users choose to enter the details manually, they can provide information about the prescribed medication or tests they have undergone at a clinic. Essential details include the medication or test name, the clinic they visited, the date, and a brief description.

After saving the record, users can view it at any time via the “View Record” button. This feature not only helps users keep track of their medical history but also enables them to share this information easily with healthcare professionals when needed. 

### **_Appointment Activity_**
The Appointment feature streamlines the process of scheduling a doctor’s visit, offering users a convenient way to book an appointment at their preferred clinic.
Users can select a clinic from a dropdown list and provide essential details such as their phone number, desired appointment date and time, and consultation specifics.
This enables users to easily secure their spot. 

To ensure accuracy and avoid any confusion or inconvenience for both users and clinics, a confirmation popup will appear once the users press the “Book Appointment” button. 
This verification step helps minimize errors and provides a seamless experience for users when scheduling appointments with healthcare professionals. 
Furthermore, On-Care application allows users to view their booked appointments, giving them an overview of upcoming visits and enabling them to better plan their time. 
This feature also helps users stay organised and avoid missing important appointments.

After users confirm their appointment by pressing the “Confirm Appointment” button, they will be directed to the subsequent screen displaying a summary of their booked appointment. 
Users have the option to cancel the appointment by pressing the minus sign located on the right side.

### **_News Activity_**
The News feature in the app is curated to provide users with relevant and up-to-date information by displaying news articles from three distinct categories: _general health, diabetes, and blood pressure. _
This activity provides an overview of each news article, displaying the date, title, and a brief description. 
This tailored approach ensures that users have access to a wealth of knowledge and insights related to their specific health interests, empowering them to stay informed.

When users find an article they are interested in and want to learn more about, they can simply tap on the “Read More” button. This action will take them to the next activity, which displays the full article.
This user-friendly design makes it easy for users to access comprehensive information, further enhancing their overall experience with the app.

### **_Health Activity_**
The Health section comprises of a list of questions covering various aspects of the user’s health, such as medical history, allergies, and sleep hours. 
This personalised health assessment serves as a baseline for tracking changes in the user’s health over time. 
Once users click on “Create my Health”, their entered data will be saved and displayed within the Health section. 
This allows users to conveniently revisit and review their health conditions whenever necessary. 
It’s important for users to provide accurate and honest responses to the questions in order to obtain a reliable assessment of their health. 

It is essential to note that no changes are allowed once the record is saved. 
The purpose of the Health section is to enable users to compare their initial health condition with their progress over time, observing whether there has been any improvement or deterioration. 
As a result, users should ensure that all details are entered accurately and thoroughly before saving the record. 
This feature encourages users to take responsibility for their health and actively monitor any changes, empowering them to make informed decision about their well-being and take appropriate actions when needed. 

### **_Chatbot Activity_**
The On-Care chatbot, powered by Google’s DialogFlow CX, provides users with instant access to a wealth of health-related information, helping them better understand their symptoms, conditions, and potential treatment options. 
By leveraging the advanced natural language processing capabilities of DialogFlow CX, the chatbot can efficiently comprehend user queries and deliver accurate and relevant responses.

Additionally, the chatbot offers guidance on appropriate next steps, such as seeking medical attention or taking self-care measures, based on the users’ symptoms.
This feature empowers users to make informed decisions about their health and helps reduce the burden on healthcare professionals by addressing common concerns remotely.

One of the primary advantages of our chatbot is that it enables users to access healthcare services without the need to wait for an appointment with a healthcare professional. 
This increased accessibility is particularly valuable in times of high demand or limited availability, ensuring that users can obtain essential health information quickly and conveniently. 

### **_Profile Activity_**
In the Profile section, all the information provided by users during the registration process is displayed. 
Users have the option to update or modify their personal details in this section. 
To ensure security, users are required to enter their password before any changes can be successfully saved. 

Additionally, there is a reset password feature available in the Profile section. 
Users can request a password reset by entering their email address, after which a link will be sent to them to complete the process. 
This feature enhances user convenience and security, allowing them to easily manage their account credentials. 
