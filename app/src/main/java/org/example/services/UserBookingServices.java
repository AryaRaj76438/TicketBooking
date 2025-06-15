package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingServices {
    private User user;
    private List<User> userList;

    private static final String USER_PATH = "../localDb/user.json";
    private ObjectMapper objectMapper = new ObjectMapper();

    public UserBookingServices(User user1) throws IOException {
        this.user = user1;
        loadUser();
    }

    private void loadUser() throws IOException {
        File users = new File(USER_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public UserBookingServices() throws IOException {
        loadUser();
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream()
                .filter(user1 -> {
                    return user1.getName().equals(user.getName()) &&
                            UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
                }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException exception){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File userFile = new File(USER_PATH);
        objectMapper.writeValue(userFile, userList);
    }

    public void fetchBooking(){
        user.printTicket();
    }

    public Boolean cancelBooking(String ticketId){
        boolean removeIf = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
        return removeIf;
    }
}
