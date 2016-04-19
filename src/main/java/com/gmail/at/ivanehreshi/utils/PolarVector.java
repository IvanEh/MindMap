package com.gmail.at.ivanehreshi.utils;

public class PolarVector {
    double rho;
    double phi;

    public PolarVector(double rho, double phi) {
        this.rho = rho;
        this.phi = phi;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }
}
