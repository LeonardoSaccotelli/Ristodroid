package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import controllers.Utility;

public class Allergenic implements Parcelable {
    private int id;
    private String name;

    public Allergenic(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Allergenic(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Allergenic> CREATOR = new Creator<Allergenic>() {
        @Override
        public Allergenic createFromParcel(Parcel in) {
            return new Allergenic(in);
        }

        @Override
        public Allergenic[] newArray(int size) {
            return new Allergenic[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Allergenic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Allergenic)) return false;
        Allergenic that = (Allergenic) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public static String getAllergenicsToString (List<Allergenic> allergenics) {
        ArrayList<String> list = new ArrayList<>();
        for(int i=0; i<allergenics.size(); i++) {
            list.add(allergenics.get(i).getName());
        }
        return Utility.listToStringWithDelimiter(list, ", ");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
