package POJO;

public class Exchange {
    public void setResult(double result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    double result;
    boolean success;
    String date;
    Info info = new Info();
    Query query = new Query();

    public double getResult() {
        return result;
    }

    public Info getInfo() {
        return info;
    }

    public Query getQuery() {
        return query;
    }
}