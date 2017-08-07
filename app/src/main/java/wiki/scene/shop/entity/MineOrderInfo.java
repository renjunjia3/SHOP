package wiki.scene.shop.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 订单信息
 * Created by scene on 17-8-1.
 */

public class MineOrderInfo implements Serializable {
    private String cycle_id;
    private int type;
    private String thumb;
    private String title;
    private String cycle_code;
    private int status;
    private String open_time;
    private String lucky_code;
    private String lucky_user_id;
    private int need_source;
    private int current_source;
    private String order_id;
    private String user_id;
    private List<String> my_codes;
    private int my_buy_number;

    public String getCycle_id() {
        return cycle_id;
    }

    public void setCycle_id(String cycle_id) {
        this.cycle_id = cycle_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCycle_code() {
        return cycle_code;
    }

    public void setCycle_code(String cycle_code) {
        this.cycle_code = cycle_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getLucky_code() {
        return lucky_code;
    }

    public void setLucky_code(String lucky_code) {
        this.lucky_code = lucky_code;
    }

    public String getLucky_user_id() {
        return lucky_user_id;
    }

    public void setLucky_user_id(String lucky_user_id) {
        this.lucky_user_id = lucky_user_id;
    }

    public int getNeed_source() {
        return need_source;
    }

    public void setNeed_source(int need_source) {
        this.need_source = need_source;
    }

    public int getCurrent_source() {
        return current_source;
    }

    public void setCurrent_source(int current_source) {
        this.current_source = current_source;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getMy_codes() {
        return my_codes;
    }

    public void setMy_codes(List<String> my_codes) {
        this.my_codes = my_codes;
    }

    public int getMy_buy_number() {
        return my_buy_number;
    }

    public void setMy_buy_number(int my_buy_number) {
        this.my_buy_number = my_buy_number;
    }
}