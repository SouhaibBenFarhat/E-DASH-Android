package com.mobile.esprit.sensor.Utils;

import com.mobile.esprit.sensor.Entities.Aroma;
import com.mobile.esprit.sensor.Entities.AromePerRecipe;
import com.mobile.esprit.sensor.Entities.Base;
import com.mobile.esprit.sensor.Entities.Boitier;
import com.mobile.esprit.sensor.Entities.Category;
import com.mobile.esprit.sensor.Entities.Comment;
import com.mobile.esprit.sensor.Entities.DeviceConfig;
import com.mobile.esprit.sensor.Entities.DeviceConfigRecipe;
import com.mobile.esprit.sensor.Entities.Manufacturer;
import com.mobile.esprit.sensor.Entities.Recipe;
import com.mobile.esprit.sensor.Entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Souhaib on 01/04/2017.
 */

public class JSONParser {

    public Boitier parseBoitier(JSONObject jsonBoitier) {

        Boitier boitier
                = new Boitier();

        try {
            boitier.setId(jsonBoitier.getInt("id"));
            boitier.setDate(jsonBoitier.getString("date"));
            boitier.setMacAddress(jsonBoitier.getString("macAddress"));
            boitier.setEnabled(jsonBoitier.getBoolean("enabled"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return boitier;

    }


    public Base parseBase(JSONObject jsonBase) {


        Base base = new Base();

        try {

            base.setIdBase(jsonBase.getInt("id"));
            base.setPg(jsonBase.getInt("pg"));
            base.setVg(jsonBase.getInt("vg"));
            base.setNicotine(jsonBase.getInt("nicotine"));
            base.setDate(jsonBase.getString("date"));
            base.setDescription(jsonBase.getString("description"));
            base.setImage(jsonBase.getString("imageUrl"));
            base.setMobileImageUrl(jsonBase.getString("mobileImageUrl"));
        } catch (JSONException e) {

        }
        return base;
    }

    public Category parseCategory(JSONObject jsonCategory) {

        Category category = new Category();

        try {

            category.setIdCategory(jsonCategory.getInt("id"));
            category.setName(jsonCategory.getString("category"));
            category.setImageName(jsonCategory.getString("imageName"));
            category.setDescription(jsonCategory.getString("description"));
            category.setImage(jsonCategory.getString("imageUrl"));
            category.setDate(jsonCategory.getString("date"));

        } catch (JSONException e) {

        }
        return category;
    }

    public Manufacturer parseManufacturer(JSONObject jsonManufacturer) {

        Manufacturer manufacturer
                = new Manufacturer();

        try {
            manufacturer.setIdManufacturer(jsonManufacturer.getInt("id"));
            manufacturer.setName(jsonManufacturer.getString("name"));
            manufacturer.setDescription(jsonManufacturer.getString("description"));
            manufacturer.setImage(jsonManufacturer.getString("imageUrl"));
            manufacturer.setAddress(jsonManufacturer.getString("address"));
            manufacturer.setDate(jsonManufacturer.getString("date"));
            manufacturer.setLang(jsonManufacturer.getInt("lang"));
            manufacturer.setLat(jsonManufacturer.getInt("lat"));
        } catch (JSONException e) {

        }
        return manufacturer;
    }

    public Aroma parseAroma(JSONObject jsonAroma) {
        Aroma aroma = new Aroma();

        try {
            aroma.setIdAroma(jsonAroma.getInt("id"));
            aroma.setName(jsonAroma.getString("arome"));
            aroma.setImgArome(jsonAroma.getString("imageUrl"));
            aroma.setDescription(jsonAroma.getString("description"));
            aroma.setDate(jsonAroma.getString("date"));
            aroma.setEnabled(jsonAroma.getBoolean("enabled"));
            aroma.setManufacturer(this.parseManufacturer(jsonAroma.getJSONObject("manufacture")));
            aroma.setCategory(this.parseCategory(jsonAroma.getJSONObject("category")));
            try {
                aroma.setUser(this.parseUser(jsonAroma.getJSONObject("user")));
            } catch (JSONException e) {

            }
        } catch (JSONException e) {

        }
        return aroma;
    }

    public User parseUser(JSONObject jsonUser) {

        User user = new User();
        try {

            user.setProfilePicture(jsonUser.getString("profilePicture"));
            user.setProvider(jsonUser.getString("provider"));
            user.setProfileId(jsonUser.getString("profileId"));
            user.setPassword(jsonUser.getString("password"));
            user.setEmail(jsonUser.getString("email"));
            user.setFirstName(jsonUser.getString("firstName"));
            user.setLastName(jsonUser.getString("lastName"));
            user.setId(jsonUser.getString("id"));
            user.setLinkUri(jsonUser.getString("linkUri"));
            user.setLogin(jsonUser.getString("login"));
            try {
                user.setBoitier(this.parseBoitier(jsonUser.getJSONObject("boitier")));
            } catch (JSONException e) {

            }

        } catch (JSONException e) {

            return null;
        }
        return user;
    }


    public AromePerRecipe parseAromePerRecipe(JSONObject jsonAromaPerRecipe) {
        AromePerRecipe aromePerRecipe = new AromePerRecipe();
        try {

            aromePerRecipe.setId(jsonAromaPerRecipe.getInt("id"));
            aromePerRecipe.setQuantity((float) jsonAromaPerRecipe.getDouble("quantity"));
            aromePerRecipe.setPosition(jsonAromaPerRecipe.getInt("position"));
            aromePerRecipe.setArome(this.parseAroma(jsonAromaPerRecipe.getJSONObject("arome")));


        } catch (JSONException e) {

        }
        return aromePerRecipe;
    }

    public DeviceConfig parseDeviceConfig(JSONObject jsonDeviceConfig) {

        DeviceConfig deviceConfig = new DeviceConfig();
        ArrayList<AromePerRecipe> aromes = new ArrayList<>();


        try {
            deviceConfig.setId(jsonDeviceConfig.getInt("id"));
            deviceConfig.setNote(jsonDeviceConfig.getString("note"));
            deviceConfig.setDate(jsonDeviceConfig.getString("date"));
            deviceConfig.setName(jsonDeviceConfig.getString("name"));
            deviceConfig.setBaseQuantity(jsonDeviceConfig.getInt("baseQuantity"));
            deviceConfig.setVolume((float) jsonDeviceConfig.getDouble("volume"));
            deviceConfig.setTotalAromeQuantity((float) jsonDeviceConfig.getDouble("totalAromeQuantity"));
            deviceConfig.setVisibility(jsonDeviceConfig.getBoolean("visibility"));
            deviceConfig.setDefault(jsonDeviceConfig.getBoolean("isDefault"));
            deviceConfig.setDeviceConfigTag(jsonDeviceConfig.getInt("deviceConfigTag"));
            deviceConfig.setBase(this.parseBase(jsonDeviceConfig.getJSONObject("base")));
            deviceConfig.setUser(this.parseUser(jsonDeviceConfig.getJSONObject("user")));


            int aromesCount = 0;
            JSONArray jsonAromes = jsonDeviceConfig.getJSONArray("aromes");
            while (aromesCount < jsonAromes.length()) {

                aromes.add(this.parseAromePerRecipe(jsonAromes.getJSONObject(aromesCount)));
                aromesCount++;

            }
            deviceConfig.setAromas(aromes);

        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }


        return deviceConfig;
    }


    public DeviceConfig parseDeviceConfigHistory(JSONObject jsonDeviceConfigHistory) {

        DeviceConfig deviceConfig = new DeviceConfig();
        ArrayList<AromePerRecipe> aromes = new ArrayList<>();


        try {
            deviceConfig.setId(jsonDeviceConfigHistory.getInt("id"));
            deviceConfig.setNote(jsonDeviceConfigHistory.getString("note"));
            deviceConfig.setDate(jsonDeviceConfigHistory.getString("date"));
            deviceConfig.setName(jsonDeviceConfigHistory.getString("name"));
            deviceConfig.setBaseQuantity(jsonDeviceConfigHistory.getInt("baseQuantity"));
            deviceConfig.setVolume((float) jsonDeviceConfigHistory.getDouble("volume"));
            deviceConfig.setTotalAromeQuantity((float) jsonDeviceConfigHistory.getDouble("totalAromeQuantity"));
            deviceConfig.setVisibility(jsonDeviceConfigHistory.getBoolean("visibility"));
            deviceConfig.setDefault(jsonDeviceConfigHistory.getBoolean("isDefault"));
            deviceConfig.setBase(this.parseBase(jsonDeviceConfigHistory.getJSONObject("base")));
            deviceConfig.setUser(this.parseUser(jsonDeviceConfigHistory.getJSONObject("user")));


            int aromesCount = 0;
            JSONArray jsonAromes = jsonDeviceConfigHistory.getJSONArray("aromes");
            while (aromesCount < jsonAromes.length()) {

                aromes.add(this.parseAromePerRecipe(jsonAromes.getJSONObject(aromesCount)));
                aromesCount++;

            }
            deviceConfig.setAromas(aromes);

        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }


        return deviceConfig;
    }

    public Recipe parseRecipe(JSONObject jsonRecipe) {

        Recipe recipe = new Recipe();
        ArrayList<AromePerRecipe> aromes = new ArrayList<>();


        try {
            recipe.setId(jsonRecipe.getInt("id"));
            recipe.setDescription(jsonRecipe.getString("description"));
            recipe.setDate(jsonRecipe.getString("date"));
            recipe.setImageUrl(jsonRecipe.getString("imageUrl"));
            recipe.setName(jsonRecipe.getString("name"));
            recipe.setSteep(jsonRecipe.getInt("stip"));
            recipe.setBaseQuantity(jsonRecipe.getInt("baseQuantity"));
            recipe.setVolume((float) jsonRecipe.getDouble("volume"));
            recipe.setComments(jsonRecipe.getInt("comments"));
            recipe.setLikes(jsonRecipe.getInt("likes"));
            recipe.setVotes(jsonRecipe.getInt("votes"));
            recipe.setTotalAromeQuantity((float) jsonRecipe.getDouble("totalAromeQuantity"));
            recipe.setRecipeTag(jsonRecipe.getInt("recipeTag"));
            recipe.setBase(this.parseBase(jsonRecipe.getJSONObject("base")));
            try {
                recipe.setUser(this.parseUser(jsonRecipe.getJSONObject("user")));
            } catch (JSONException e) {

            }


            int aromesCount = 0;
            JSONArray jsonAromes = jsonRecipe.getJSONArray("aromes");
            while (aromesCount < jsonAromes.length()) {

                aromes.add(this.parseAromePerRecipe(jsonAromes.getJSONObject(aromesCount)));
                aromesCount++;

            }
            recipe.setAromes(aromes);

        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }


        return recipe;
    }

    public Comment parseComment(JSONObject jsonComment) {

        Comment comment
                = new Comment();
        try {
            comment.setId(jsonComment.getInt("id"));
            comment.setContent(jsonComment.getString("content"));
            comment.setDate(jsonComment.getString("date"));
            comment.setUser(this.parseUser(jsonComment.getJSONObject("user")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return comment;
    }


    public DeviceConfigRecipe parseDeviceConfigRecipe(JSONObject jsonDeviceConfigRecipe) {

        ArrayList<AromePerRecipe> aromes = new ArrayList<>();
        DeviceConfigRecipe deviceConfigRecipe = new DeviceConfigRecipe();
        try {
            deviceConfigRecipe.setId(jsonDeviceConfigRecipe.getInt("id"));
            deviceConfigRecipe.setDescription(jsonDeviceConfigRecipe.getString("description"));
            deviceConfigRecipe.setDate(jsonDeviceConfigRecipe.getString("date"));
            deviceConfigRecipe.setImageUrl(jsonDeviceConfigRecipe.getString("imageUrl"));
            deviceConfigRecipe.setName(jsonDeviceConfigRecipe.getString("name"));
            deviceConfigRecipe.setSteep(jsonDeviceConfigRecipe.getInt("stip"));
            deviceConfigRecipe.setBaseQuantity(Float.parseFloat(jsonDeviceConfigRecipe.getString("baseQuantity")));
            deviceConfigRecipe.setVolume(Float.parseFloat(jsonDeviceConfigRecipe.getString("volume")));
            deviceConfigRecipe.setComments(jsonDeviceConfigRecipe.getInt("comments"));
            deviceConfigRecipe.setLikes(jsonDeviceConfigRecipe.getInt("likes"));
            deviceConfigRecipe.setVotes(jsonDeviceConfigRecipe.getInt("votes"));
            deviceConfigRecipe.setTotalAromeQuantity((float) jsonDeviceConfigRecipe.getDouble("totalAromeQuantity"));
            deviceConfigRecipe.setBase(this.parseBase(jsonDeviceConfigRecipe.getJSONObject("base")));
            deviceConfigRecipe.setUser(this.parseUser(jsonDeviceConfigRecipe.getJSONObject("user")));
            deviceConfigRecipe.setDeviceConfig(this.parseDeviceConfig(jsonDeviceConfigRecipe.getJSONObject("deviceConfig")));
            int aromesCount = 0;
            JSONArray jsonAromes = jsonDeviceConfigRecipe.getJSONArray("aromes");
            while (aromesCount < jsonAromes.length()) {

                aromes.add(this.parseAromePerRecipe(jsonAromes.getJSONObject(aromesCount)));
                aromesCount++;

            }
            deviceConfigRecipe.setAromes(aromes);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return deviceConfigRecipe;
    }

}
