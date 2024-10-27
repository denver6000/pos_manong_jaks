package com.denproj.posmanongjaks.session;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.denproj.posmanongjaks.model.Branch;
import com.denproj.posmanongjaks.model.Role;
import com.denproj.posmanongjaks.model.User;

public class Session implements Parcelable {
    private Branch branch;
    private User user;
    private Role role;

    private boolean isConnectionReachable;

    public Session(Branch branch, User user, Role role) {
        this.branch = branch;
        this.user = user;
        this.role = role;
    }

    public Session() {
    }

    protected Session(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        isConnectionReachable = in.readByte() != 0;
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isConnectionReachable() {
        return isConnectionReachable;
    }

    public void setConnectionReachable(boolean connectionReachable) {
        isConnectionReachable = connectionReachable;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(user, i);
        parcel.writeByte((byte) (isConnectionReachable ? 1 : 0));
    }
}
