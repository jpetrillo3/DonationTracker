package kantwonskids.donationtrackerg14b.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * @author Ethan Wilson <ewilson72@gatech.edu>
 * @version 1.1
 *
 * A basic user class
 */
public class User implements Parcelable, Searchable, Serializable {

    private final String username;
    private final String password;
    private final UserRole role;
    @Nullable
    private final OurLocation location;

    /**
     * Creates a new user with a given username, password, role, and assigned location.
     * The location must be null for all roles but location employees.
     *
     * @param username the username
     * @param password the password
     * @param role the user's role
     * @param location the user's location
     * @throws IllegalArgumentException If the role is not location employee and "location"
     * is anything other than null
     */
    public User(String username, String password, UserRole role, @Nullable OurLocation location) {
        this.username = username;
        this.password = password;
        this.role = role;
        if ((location != null) && (role != UserRole.LOCATION_EMPLOYEE)) {
            throw new IllegalArgumentException("Only location employees can have assigned" +
                    "locations");
        }
        this.location = location;

    }

    /**
     * Creates a new user with a given username, password, and role.
     * If the new user is a location employee, an exception is thrown, as the location must be
     * specified.
     *
     * @param username the username
     * @param password the password
     * @param role the user's role
     */
    public User(String username, String password, UserRole role) {
        this(username, password, role, null);
        if (role == UserRole.LOCATION_EMPLOYEE) {
            throw new IllegalArgumentException("A location must be specified for " +
                    "location employees.");
        }
    }

    /**
     * Create a new user from a parcel.
     * @param in The parcel to create a new user from.
     */
    private User(Parcel in) {
        this.username = in.readString();
        this.password = in.readString();
        // this.location = in.readString(); idk what this parcel thing is but it is bad
        this.location = null;
        this.role = (UserRole)in.readSerializable();
    }

    /**
     * Returns a boolean value determining whether this user can update info at a certain location.
     * @param loc the location to update info at
     * @return true if the user can, false if it cannot
     * @throws IllegalArgumentException if the location is null
     */
    public boolean canUpdateDonationsAt(OurLocation loc) {
        if (loc == null) {
            throw new IllegalArgumentException("Location cannot be null.");
        }

        // Managers/admins can add/remove donations at all locations
        if (role.canAddOrRemoveDonationsAllLocations()) {
            return true;
        }

        // Location employees can add/remove if the location matches
        if (role.canAddOrRemoveDonationsSpecificLocation()) {
            return loc.equals(location);
        }

        // Everybody else can do nothing
        return false;
    }

    /**
     * Gets the username
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Parcelable creator.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.password);
        // dest.writeString(this.location);
        dest.writeSerializable(this.role);
    }

    @Override
    public boolean equals(Object other) {
        if ((other == null) || !(other instanceof User)) {
            return false;
        }

        User u = (User) other;
        return this.username.equals(u.getUsername());
    }

    @Override
    public int hashCode() {
        return this.username.hashCode() + 17;
    }

    @Override
    public String getLabel() {
        return this.getUsername();
    }
}