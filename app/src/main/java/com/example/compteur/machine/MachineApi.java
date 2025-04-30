package com.example.compteur.machine;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface MachineApi {

    @GET("machines")
    Call<List<Machine>> getAllMachines();

    @GET("machines/{id}")
    Call<Machine> getMachineById(@Path("id") Long id);

    @POST("machines")
    Call<Machine> createMachine(@Body Machine machine);

    @PUT("machines/{id}")
    Call<Machine> updateMachine(@Path("id") Long id, @Body Machine machine);

    @DELETE("machines/{id}")
    Call<Void> deleteMachine(@Path("id") Long id);
}
