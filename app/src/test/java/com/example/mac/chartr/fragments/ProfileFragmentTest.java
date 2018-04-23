package com.example.mac.chartr.fragments;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mac.chartr.ApiClient;
import com.example.mac.chartr.ApiInterface;
import com.example.mac.chartr.objects.Review;
import com.example.mac.chartr.objects.Trip;
import com.example.mac.chartr.objects.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by cygnus on 3/7/18.
 * <p>
 * Used to test the ProfileFragment
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProfileFragment.class, ApiClient.class})
public class ProfileFragmentTest {
    @Test
    public void testOnCreateView() {
        ProfileFragment victim = PowerMockito.mock(ProfileFragment.class);
        PowerMockito.mockStatic(ApiClient.class);

        LayoutInflater inflater = mock(LayoutInflater.class);
        ViewGroup viewGroup = mock(ViewGroup.class);
        Bundle bundle = mock(Bundle.class);
        AppCompatActivity appCompatActivity = mock(AppCompatActivity.class);
        ActionBar actionBar = mock(ActionBar.class);
        View view = mock(View.class);
        Button button = mock(Button.class);

        PowerMockito.when(victim.getActivity()).thenReturn(appCompatActivity);
        PowerMockito.when(ApiClient.getApiInstance()).thenReturn(new ApiInterface() {
            @Override
            public Call<User> getUserFromEmail(String email) {
                return null;
            }

            @Override
            public Call<User> getUserFromUid(String uid) {
                return new Call<User>() {
                    @Override
                    public Response<User> execute() throws IOException {
                        return null;
                    }

                    @Override
                    public void enqueue(Callback<User> callback) {

                    }

                    @Override
                    public boolean isExecuted() {
                        return false;
                    }

                    @Override
                    public void cancel() {

                    }

                    @Override
                    public boolean isCanceled() {
                        return false;
                    }

                    @Override
                    public Call<User> clone() {
                        return null;
                    }

                    @Override
                    public Request request() {
                        return null;
                    }
                };
            }

            @Override
            public Call<Void> postUser(User user) {
                return null;
            }

            @Override
            public Call<Void> deleteUser(String uid) {
                return null;
            }

            @Override
            public Call<List<Trip>> getAllTrips() {
                return null;
            }

            @Override
            public Call<List<Trip>> getAllCurrentTrips() {
                return null;
            }

            @Override
            public Call<List<Trip>> getAllUserTrips(String uid) {
                return null;
            }

            @Override
            public Call<List<Trip>> getUserTripsForStatus(String uid, String status) {
                return null;
            }

            @Override
            public Call<Trip> getTrip(String tid) {
                return null;
            }

            @Override
            public Call<String> updateTrip(String uid, String tid, String status) {
                return null;
            }

            @Override
            public Call<String> postUserDrivingTrip(String uid, Trip trip) {
                return null;
            }

            @Override
            public Call<Void> deleteTrip(String tid) {
                return null;
            }

            @Override
            public Call<String> postReviewFromUser(String uid, Review review) {
                return null;
            }

            @Override
            public Call<Review> getReview(String rid) {
                return null;
            }

            @Override
            public Call<List<Review>> getAllReviewsForUser(String uid) {
                return null;
            }
        });
        when(appCompatActivity.getSupportActionBar()).thenReturn(actionBar);
        when(appCompatActivity.findViewById(any(int.class))).thenReturn(button);
        when(inflater.inflate(any(int.class), any(ViewGroup.class), any(boolean.class)))
                .thenReturn(view);
        when(view.findViewById(any(int.class))).thenReturn(button);
        when(victim.onCreateView(inflater, viewGroup, bundle)).thenCallRealMethod();

        View root = victim.onCreateView(inflater, viewGroup, bundle);

        assertNotNull(root);
    }
}
