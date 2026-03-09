package com.ums_crm.userManagement.schoolDTO;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Table(name = "school")
@Entity
public class SchoolDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String vschool_name;
    private String vschool_location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVschool_name() {
        return vschool_name;
    }

    public void setVschool_name(String vschool_name) {
        this.vschool_name = vschool_name;
    }

    public String getVschool_location() {
        return vschool_location;
    }

    public void setVschool_location(String vschool_location) {
        this.vschool_location = vschool_location;
    }
}
