package mary.travelcards;


import java.util.List;

public class CompaniesWrapper extends Wrapper {

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    private List<Company> companies;
}
