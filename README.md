# Math Tutoring Agent
This software package contains the code for a furhat math tutoring skill, which includes functionalities that allow the agent to recognize user emotions via visual input and change its affectual behaviour accordingly.


## Setup
In this section we will give a step-by-step walkthrough of setting up the entire software package.

### Furhat
Furhat is the conversational framework that we have used to build this bot with. This choice was based on the large range of functionalities it has, including all that we deemed necessary for constructing an efficient and pleasant learning environment for the students that where using our tool. That's why, to get started, we need our furhat sdk running so we can connect our skill to it at a later stage. 

First of all, download the open source furhat sdk which you can do by requesting it on their website at https://furhatrobotics.com/furhat-sdk. Extract the zip files any place you can find it again. To run the SDK, simply navigate to the folder named "Furhat-SDK-OS-x".x.x. Where the x's denote some version and OS your operating system. Then, open your command prompt in this folder and execute the line below.

    launchSDK.bat

A process will start that includes a graphical interface witg the furhat bot. You can now use your browser to connect to localhost:8080, which is the address at which the furhat web interface is hosting, use the password 'admin' to gain access. There is a range of settings you can adjust in this panel but for now it is not of great importance.

### Emotion detection
To allow us to use all functionalities coming with our conversational agents, we need to first start a piece of software that detects user emotions. This piece of software uses a pre-trained convolutional neural network to allow the recognition and emotion classification of faces on a web server. There are a number of additional dependencies that are required for this webserver to run, namely the below stated libraries.

    numpy==1.17.4
    opencv-python==4.1.2.30
    tensorflow==2.1.2
    flask => latest version

You can install these using pip install. To now run the webserver, open the ".../code/CA_emotion_detector" folder and run webserver.py using python3. You can test whether your emotion detection model is set up properly by going to localhost:5000, it should take a snapshot of your webcam input and return the detected affect.

### Math Tutoring Skill
In the web interface it will say "no skill running" in the top left corner. To connect our math tutoring skill with the furhat SDK, open the ".../code/MathTutor" folder in an IDE with kotlin support. Run the main method in main.kt to run the skill. It automatically connects to the SDK and you should now be able to see the skill running where it first said there was none running.

## What's next?
You are now ready to start learning some math skils! Go to the wizard tab in the web interface and double click in the territory of the agent, it will automatically start the conversation by asking you how it can help you.