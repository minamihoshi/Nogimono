package org.nogizaka46.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * Created by acer on 2017/4/15.
 */
@Table("image")
public class WithpicBean implements Serializable {

        /**
         * image : https://platform.idolx46.top/photo/646480/587454e923f04c96ed61cf5bfba280d5.jpg
         */
        @PrimaryKey(AssignType.BY_MYSELF)
        @Column("image")
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }


    @Override
    public String toString() {
        return "WithpicBean{" +
                "image='" + image + '\'' +
                '}';
    }
}

