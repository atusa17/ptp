package edu.msudenver.tsp.website.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProofDriver {
    List<String> theromList = new ArrayList<String>();
    public String processProof(String theoremName){
        //Business Logic
        return theoremName + "Accepted";
    }

}
