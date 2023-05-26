# M7019E

Blog app

# Setting up the project
1. Clone the repository
2. To setup the backend follow the `README.md` intructions found in the Backend folder 
3. To setup the app follow the `README.md` instructions found in the frontend folder

# Running the project
1. Start docker desktop
2. Navigate in a terminal to the backend folder and setup the docker containers using `docker compose up -d --build --no-deps`
    - Note, you may have to restart all containers except `rabbitmq` in order for the connections to set up properly
4. For manual testing of the api calls to the server open swagger ui by going to `localhost:5001/docs` in your browser
5. Navigate to the frontend folder and open the containing project in android studio
6. Run the app on an emulator or physical device

# Acknowledgements
The backend part of this project was created in the course `M7011E` (2022) by the group gamla och bittra; with Elliot Huber, Magnus Stenfelt, Peter Panduro and Tovah Parnes.
