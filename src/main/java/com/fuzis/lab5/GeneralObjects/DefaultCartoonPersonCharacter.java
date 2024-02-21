package com.fuzis.lab5.GeneralObjects;

import com.fuzis.lab5.Enums.Opinion;
import com.fuzis.lab5.Enums.Popularity;
import com.fuzis.lab5.Enums.Sex;
import com.fuzis.lab5.Interaces.IPersonCharacter;
import com.fuzis.lab5.Exception.UndefinedOpinionException;
import java.util.*;

/**
 * Основной класс персонажа, содержащий 12 полей:
 * <code>String name</code>,
 * <code>Sex sex</code>,
 * <code>String quote</code>,
 * <code>{@literal Map<String,Opinion>} Opinions</code> - мнения данного персонажа о других персонажах,
 * <code>Double height</code> - рост данного персонажа,
 * <code>Double weight - вес данного персонажа</code>,
 * <code>Boolean isAnimeCharacter</code> - является ли данный персонаж персонажем аниме?,
 * <code>Popularity popularity</code> - популярность данного персонажа,
 * <code>String description</code>, - краткое описание данного персонажа
 * <code>Integer health</code>, - здоровье данного персонажа в целых условных единицах
 * <code>Double age</code>, - возраст данного персонажа
 * <code>List{@literal <String>} additionalNames</code> - другие имена того же персонажа
 *<br>
 * <img width="500" src="https://images.stopgame.ru/uploads/users/2021/432959/00011.SAe_eHU.jpg">
 */
public class DefaultCartoonPersonCharacter implements IPersonCharacter,Comparable<DefaultCartoonPersonCharacter> {

    public DefaultCartoonPersonCharacter(String name, Sex sex) {
        this(name,sex,null,null,null,null,null);
    }
    public DefaultCartoonPersonCharacter(String name, Sex sex, String quote, Double height,Double weight,Popularity popularity,String description) {
        this(name,sex,quote,null,null,height,weight,null,popularity,description,null,null);
    }
    public DefaultCartoonPersonCharacter(String name, Sex sex,String quote,Map<String, Opinion> opinions,List<String> additional_names,Double height,Double weight,Boolean isAnimeCharacter,Popularity popularity,String description, Double age,Integer health) {
        this.name = name;
        this.sex = sex;
        if(quote != null) this.quote = quote;
        if(opinions != null)this.opinions = new HashMap<>(opinions);
        else this.opinions = new HashMap<>();
        if(additional_names != null)this.additionalNames = new ArrayList<>(additional_names);
        this.height = height;
        this.weight = weight;
        this.isAnimeCharacter = isAnimeCharacter;
        this.popularity = popularity;
        this.description = description;
        this.health = health;
        this.age = age;
    }
    //Возможности изменения цитаты, имени, пола и прочего оставим на наследников
    protected String name;
    protected Sex sex;
    protected String quote = "It's cool!";
    protected Map<String, Opinion> opinions;
    protected List<String> additionalNames;
    protected Double height;
    protected Double weight;
    protected Boolean isAnimeCharacter;
    protected Popularity popularity;
    protected String description;
    protected Integer health;
    protected Double age;
    @Override
    public String getQuote() {
        return "\"" + this.quote + "\"";
    }

    @Override
    public Sex getSex() {
        return this.sex;
    }

    public Integer getHealth() {
        return health;
    }

    public Double getAge() {
        return age;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public Map<String, Opinion> getOpinions() {
        return opinions;
    }

    public List<String> getAdditionalNames() {
        return additionalNames;
    }

    public Double getHeight() {
        return height;
    }

    public Double getWeight() {
        return weight;
    }

    public Boolean getAnimeCharacter() {
        return isAnimeCharacter;
    }

    public Popularity getPopularity() {
        return popularity;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Простая затычка возможности мечтать
     * @return у {@literal <имя>} нет мечты !!! ~_~
     */
    @Override
    public String toDream() {
        return "у " + this.getNPossesivePronoun() + " нет мечты!!! ~_~";
    }

    /**
     * Затычка вместо ворчания, ведь кто-то разве должен ворчать в нашем замечательном мире?!
     * @return "Ворчать - плохо!!!"
     */
    @Override
    public String toGrumble() {
        return "Ворчать - плохо!!!";
    }

    /**
     * Возвращает мнение о персонаже
     * @param ch имя персонажа, мнение о котором необходимо узнать
     * @return мнение данного персонажа об указанном (если мнение прописано)
     * @throws UndefinedOpinionException выкидывается если мнение о данном персонаже не найдено
     */
    @Override
    public Opinion getOpinion(String ch) throws UndefinedOpinionException {
        Opinion op = this.opinions.get(ch);
        if(op==null)throw new UndefinedOpinionException();
        else return op;
    }

    @Override
    public void setOpinion(String ch, Opinion op) {
        this.opinions.put(ch,op);
    }


    @Override
    public String toString() {
        return "DefaultCartoonPersonCharacter{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", quote='" + quote + '\'' +
                ", opinions=" + opinions +
                ", additionalNames=" + additionalNames +
                ", height=" + height +
                ", weight=" + weight +
                ", isAnimeCharacter=" + isAnimeCharacter +
                ", popularity=" + popularity +
                ", description='" + description + '\'' +
                ", health=" + health +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultCartoonPersonCharacter that = (DefaultCartoonPersonCharacter) o;
        return Objects.equals(getName(), that.getName()) && getSex() == that.getSex() && Objects.equals(getQuote(), that.getQuote()) && Objects.equals(getOpinions(), that.getOpinions()) && Objects.equals(getAdditionalNames(), that.getAdditionalNames()) && Objects.equals(getHeight(), that.getHeight()) && Objects.equals(getWeight(), that.getWeight()) && Objects.equals(isAnimeCharacter, that.isAnimeCharacter) && getPopularity() == that.getPopularity() && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getHealth(), that.getHealth()) && Objects.equals(getAge(), that.getAge());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSex(), getQuote(), getOpinions(), getAdditionalNames(), getHeight(), getWeight(), isAnimeCharacter, getPopularity(), getDescription(), getHealth(), getAge());
    }

    /**
     * Сравнение сначала по имени, потом по росту, а уже по возрасту, вроде сейчас ему придают все меньшее значение ^__^ Иначе считаем что объекты равны, потому что редко у персонажей сойдутся все параметры сразу...
     * <br>
     * <img width="500" src="https://animesolution.com/wp-content/uploads/2020/01/ReZero-kara-Hajimeru-Isekai-Seikatsu-Shin-Henshuu-ban-03A_14.03_2020.01.11_23.57.23-1536x864.jpg">
     * @param ch2 Как intellij idea сказал "the object to be compared"
     * @return результат сравнения по имени, росту и возрасту
     */
    public int compareTo(DefaultCartoonPersonCharacter ch2)
    {
        if(this.equals(ch2))return 0;
        if(this.name == null)return -1;
        if(ch2.name == null)return 1;
        var name_comp = this.name.compareTo(ch2.name);
        if(name_comp != 0)return name_comp;
        if(this.height == null)return -1;
        if(ch2.height == null)return 1;
        var height_comp = this.height.compareTo(ch2.height);
        if(height_comp != 0)return height_comp;
        if(this.age == null)return -1;
        if(ch2.age == null)return 1;
        var age_comp = this.age.compareTo(ch2.age);
        if(age_comp != 0)return age_comp;
        return 0;
    }

    /**
     * Ну эта маленький метод решает целый огромный баг, связанный с тем что кавычки цитаты плодились при перезаписывании файла с огромной скоростью и могли сожрать целый мир, теперь они этого сделать не смогут!!!
     * <br>
     * <img width="500" src="https://animepersona.com/wp-content/uploads/2022/12/brm7fp6ptaj41-1024x576.webp">
     * @return просто значение поля <code>quote</code>, ничего лишнего
     */
    public String getSimpleQuote() {
        return this.quote;
    }
}
