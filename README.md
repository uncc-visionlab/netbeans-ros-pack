# netbeans-ros-pack
ROS Integrated Development Environment for the Netbeans Integrated Development Environment


This is a suite of 3 plugins for the Netbeans Integrated Development Environment that provide ROS development tools that simplify development of ROS applications.

Project types are provided for ROS workspace environments and for ROS packages within a ROS workspace. New file type recognition is provided for .cfg, .launch, .bag, .msg, and .srv files. To install: Unzip the .zip file to a folder and install the three nbm files to Netbeans as plugins. Check out the YouTube tutorial to see how it works: https://www.youtube.com/watch?v=AHX6gaz6WQA

Changed the file structure to be compatible with Remote C++ Project development. You can now build and run launch files on remote targets! Unfortunately this breaks compatibility with older ROS Project file structures. To update existing Netbeans ROS Pack projects create a new ROS project from Netbeans and copy the "ros_ws/nbproject/ros.project.properties" file and "ros_ws/Makefile" into the folders of the same name in your existing project. 
