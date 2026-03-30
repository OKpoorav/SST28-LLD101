package com.bookmyshow;

import com.bookmyshow.enums.ScreenType;
import com.bookmyshow.enums.SeatStatus;
import com.bookmyshow.enums.SeatType;
import com.bookmyshow.exception.InvalidOperationException;
import com.bookmyshow.exception.PaymentFailedException;
import com.bookmyshow.exception.SeatsUnavailableException;
import com.bookmyshow.model.Booking;
import com.bookmyshow.model.City;
import com.bookmyshow.model.Movie;
import com.bookmyshow.model.Screen;
import com.bookmyshow.model.Seat;
import com.bookmyshow.model.Show;
import com.bookmyshow.model.Theater;
import com.bookmyshow.payment.CardPayment;
import com.bookmyshow.payment.UPIPayment;
import com.bookmyshow.payment.WalletPayment;
import com.bookmyshow.service.BookingService;
import com.bookmyshow.service.ShowService;
import com.bookmyshow.user.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ShowService showService = new ShowService();
        BookingService bookingService = new BookingService();

        City mumbai = new City("C1", "Mumbai");
        Movie movie = new Movie("M1", "Inception", 148, "English", "Sci-Fi");

        Theater theater = new Theater("T1", "PVR Juhu", mumbai);
        Screen screen = new Screen("S1", "Screen 1", ScreenType.IMAX, theater);

        screen.addSeat(new Seat("SEAT1", "A", 1, SeatType.RECLINER, 500.0));
        screen.addSeat(new Seat("SEAT2", "A", 2, SeatType.RECLINER, 500.0));
        screen.addSeat(new Seat("SEAT3", "B", 1, SeatType.PREMIUM, 350.0));
        screen.addSeat(new Seat("SEAT4", "B", 2, SeatType.PREMIUM, 350.0));
        screen.addSeat(new Seat("SEAT5", "C", 1, SeatType.REGULAR, 200.0));

        theater.addScreen(screen);

        showService.addMovie(movie);
        showService.addTheater(theater);

        LocalDateTime showTime = LocalDateTime.now().plusHours(3);
        Show show = showService.scheduleShow(movie, screen, showTime);

        Customer alice = new Customer("U1", "Alice", "alice@email.com", "9999999999");
        Customer bob = new Customer("U2", "Bob", "bob@email.com", "8888888888");

        System.out.println("\n--- Step 1: View available shows ---");
        List<Show> shows = showService.getShowsForMovieInCity(movie, mumbai);
        for (Show s : shows) {
            System.out.println("Show: " + s.getMovie().getTitle() + " at " + s.getStartTime());
        }

        System.out.println("\n--- Step 2: View available seats ---");
        List<Seat> availableSeats = showService.getAvailableSeatsForShow(show);
        for (Seat seat : availableSeats) {
            System.out.println("Seat " + seat.getRow() + seat.getSeatNumber()
                    + " | Type: " + seat.getSeatType()
                    + " | Price: Rs." + seat.getPrice());
        }

        System.out.println("\n--- Step 3: Alice books two seats ---");
        List<Seat> aliceSeats = new ArrayList<>();
        aliceSeats.add(screen.getSeats().get(0));
        aliceSeats.add(screen.getSeats().get(1));
        Booking aliceBooking = bookingService.lockSeats(show, aliceSeats, alice);
        System.out.println("Booking id: " + aliceBooking.getId() + " | Total: Rs." + aliceBooking.getTotalAmount());
        UPIPayment upiProcessor = new UPIPayment("alice@upi");
        bookingService.confirmBooking(aliceBooking, upiProcessor);
        System.out.println("Alice's booking confirmed.");

        System.out.println("\n--- Step 4: Bob tries the same seats ---");
        try {
            bookingService.lockSeats(show, aliceSeats, bob);
        } catch (SeatsUnavailableException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        System.out.println("\n--- Step 5: Bob books a different seat ---");
        List<Seat> bobSeats = new ArrayList<>();
        bobSeats.add(screen.getSeats().get(2));
        Booking bobBooking = bookingService.lockSeats(show, bobSeats, bob);
        CardPayment cardProcessor = new CardPayment("4242");
        bookingService.confirmBooking(bobBooking, cardProcessor);
        System.out.println("Bob's booking confirmed.");

        System.out.println("\n--- Step 6: Alice cancels her booking ---");
        bookingService.cancelBooking(aliceBooking);
        System.out.println("Alice cancelled. Seats should be available again.");

        System.out.println("\n--- Step 7: Verify seats released ---");
        List<Seat> nowAvailable = showService.getAvailableSeatsForShow(show);
        for (Seat seat : nowAvailable) {
            System.out.println("Available: Seat " + seat.getRow() + seat.getSeatNumber()
                    + " | Type: " + seat.getSeatType()
                    + " | Price: Rs." + seat.getPrice());
        }

        System.out.println("\n--- Step 8: Test invalid operation ---");
        try {
            bookingService.cancelBooking(aliceBooking);
        } catch (InvalidOperationException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n--- Step 9: Wallet payment with insufficient funds ---");
        WalletPayment walletProcessor = new WalletPayment("bob-wallet", 100.0);
        List<Seat> seat1List = new ArrayList<>();
        seat1List.add(screen.getSeats().get(0));
        Booking walletBooking = bookingService.lockSeats(show, seat1List, bob);
        try {
            bookingService.confirmBooking(walletBooking, walletProcessor);
        } catch (PaymentFailedException e) {
            System.out.println("Caught: payment failed as expected.");
        }
        SeatStatus seat1Status = show.getSeatStatus("SEAT1");
        System.out.println("SEAT1 status after failed payment: " + seat1Status);
    }
}
