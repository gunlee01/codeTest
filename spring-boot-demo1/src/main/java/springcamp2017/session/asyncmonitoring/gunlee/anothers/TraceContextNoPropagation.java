package springcamp2017.session.asyncmonitoring.gunlee.anothers;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 16.
 */
public class TraceContextNoPropagation {
    public static void main(String[] args) {
        WelcomeController controller = new WelcomeController();
        controller.doStart(new BizStuff());
    }
}

class WelcomeController {
    void doStart(BizStuff stuff) {
        TraceContext traceContext = new TraceContext();
        traceContext.add(TraceInfo.SERVICE_START);
        welcomeBiz.doWelcomeBiz(stuff);
        springCampBiz.doSpringCampBiz(stuff);
        traceContext.add(TraceInfo.SERVICE_END);
    }

    SpringCampBiz springCampBiz;
    WelcomeBiz welcomeBiz;
}

class WelcomeBiz {
    public void doWelcomeBiz(BizStuff bizStuff) {
        welcomeDao.doWelcomeDao(bizStuff);
    }

    WelcomeDao welcomeDao;
}

class SpringCampBiz {
    public void doSpringCampBiz(BizStuff bizStuff) {

    }
}

class WelcomeDao {
    public void doWelcomeDao(BizStuff bizStuff) {

    }
}

class BizStuff {

}


class TraceContext {
    public void add(TraceCase traceInfo) {
    }
}

class TraceInfo {
    public static TraceCase SERVICE_START;
    public static TraceCase SERVICE_END;
    public static TraceCase METHOD_START;
    public static TraceCase METHOD_END;

    public TraceInfo(TraceCase traceCase) {

    }
}

class TraceCase {
}



