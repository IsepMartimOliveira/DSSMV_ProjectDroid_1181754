package com.example.onlinesupertmarket.Service;

import android.widget.ImageView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.onlinesupertmarket.DTO.FoodTriviaDTO;
import com.example.onlinesupertmarket.Mapper.Convert;
import com.example.onlinesupertmarket.Mapper.DTOMapper;
import com.example.onlinesupertmarket.Model.FoodTrivia;
import com.example.onlinesupertmarket.Network.HttpClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.example.onlinesupertmarket.Utils.Utils.*;


public class HomeViewModel extends ViewModel {

    private final MutableLiveData<FoodTrivia> triviaLiveData = new MutableLiveData<>();

    public LiveData<FoodTrivia> getTriviaLiveData() {
        return triviaLiveData;
    }

    public void getTrivia() {
        String url = apiUrl + foodTrivia+api_key;
        HttpClient.getRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                FoodTriviaDTO foodTriviaDTO = Convert.convertFromJson(responseBody, FoodTriviaDTO.class);
                DTOMapper<FoodTriviaDTO, FoodTrivia> foodTriviaDTOMapper = new DTOMapper<>(dto -> new FoodTrivia(dto.getText()));
                FoodTrivia foodTrivia = foodTriviaDTOMapper.map(foodTriviaDTO);

                triviaLiveData.postValue(foodTrivia);
            }
        });

    }
}
