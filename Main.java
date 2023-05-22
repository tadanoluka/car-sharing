package carsharing;

public class Main {

    public static void main(String[] args) {
        String databaseFileName = "carsharing.mv.db";

        if (args.length > 0 && args[0].equals("-databaseFileName")) {
            databaseFileName = args[1];
        }

        CompanyDaoImpl dao = new CompanyDaoImpl(databaseFileName);
        //dao.dropCustomerTable();
        //dao.dropCarTable();
        //dao.dropCompanyTable();

        dao.createCompanyTable();
        dao.createCarTable();
        dao.createCustomerTable();

        TUIHandler tuiHandler = new TUIHandler(dao);
        tuiHandler.run();
    }
}