package springcamp2017.session.asyncmonitoring.gunlee;

import scouter.agent.plugin.AbstractPlugin;
import scouter.agent.plugin.WrContext;
import scouter.agent.trace.HookArgs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 16.
 */
public class TraceContextPropagation {
    public static void main(String[] args) {
        WelcomeController controller = new WelcomeController();
        controller.doStart(new BizStuff());

    }
}

class Test1 {
    static ExecutorService executorService = Executors.newFixedThreadPool(3);
    SpringCampBiz springCampBiz;
    WelcomeBiz welcomeBiz;

    void doSpringCamp(final BizStuff stuff) {
        executorService.execute(new Runnable() {
            public void run() {
                springCampBiz.doSpringCampBiz(stuff);
            }
        });
    }

    class Test1$InnerClass1 implements Runnable
    {
        Test1 _this;
        BizStuff _stuff;

        Test1$InnerClass1(Test1 _this, BizStuff _stuff) {
            this._this = _this;
            this._stuff = _stuff;
        }

        public void run() {
            _this.springCampBiz.doSpringCampBiz(_stuff);
        }
    }
}

class TraceContextHoler {
    static TraceContextHoler holder;

    static void set(TraceContext t) {}
    static TraceContext get() {return null;}
}

class TraceContextTransfer {
    static TraceContextHoler holder;

    static void set(Object o, TraceContext context) {}
    static TraceContext get(Object o) {return null;}
}


class SCamp1 extends AbstractPlugin {
    void doSpringCamp(final BizStuff stuff) {
        executorService.execute(new Runnable() {
            public void run() {
                springCampBiz.doSpringCampBiz(stuff);
            }
        });
    }

    class DoSpring$InnerClass1 implements Runnable {
        DoSpring$InnerClass1(SCamp1 _parentThis, BizStuff _paramStuff) {
            TraceContextTransfer.set(this, TraceContextHoler.get()); //BCI
            this._parentThis = _parentThis;
            this._paramStuff = _paramStuff;
        }
        public void run() {
            TraceContextHoler.set(TraceContextTransfer.get(this)); //BCI
            _parentThis.springCampBiz.doSpringCampBiz(_paramStuff);
        }

        SCamp1 _parentThis;
        BizStuff _paramStuff;
    }


    void hook_method1(WrContext $ctx, HookArgs $hook) {

        //@forward(class="Test1", name="doSpringCamp", param=0)

        //@receive(class="Test1$1", name="run", param=0)


        forward($ctx, System.identityHashCode($hook.getArgs()[0]));



        receive($ctx, System.identityHashCode($hook.getArgs()[0]));

    }

    static ExecutorService executorService = Executors.newFixedThreadPool(3);
    SpringCampBiz springCampBiz;
    WelcomeBiz welcomeBiz;

}





class WelcomeController {
    public void doStart(BizStuff stuff) {
        TraceContext traceContext = new TraceContext();
        traceContext.add(new TraceInfo(TraceInfo.SERVICE_START));

        welcomeBiz.doWelcomeBiz(stuff, traceContext);
        springCampBiz.doSpringCampBiz(stuff, traceContext);

        traceContext.add(new TraceInfo(TraceInfo.SERVICE_END));
    }

    SpringCampBiz springCampBiz;
    WelcomeBiz welcomeBiz;
}

class WelcomeBiz {
    public void doWelcomeBiz(BizStuff bizStuff, TraceContext traceContext) {
        traceContext.add(new TraceInfo(TraceInfo.METHOD_START));
        welcomeDao.doWelcomeDao(bizStuff, traceContext);
        traceContext.add(new TraceInfo(TraceInfo.METHOD_END));
    }

    public void doWelcomeBiz(BizStuff bizStuff) {
        //welcomeDao.doWelcomeDao(bizStuff, traceContext);
    }

    WelcomeDao welcomeDao;
}

class SpringCampBiz {
    public void doSpringCampBiz(BizStuff bizStuff, TraceContext traceContext) {

    }

    public void doSpringCampBiz(BizStuff bizStuff) {

    }
}

class WelcomeDao {
    public void doWelcomeDao(BizStuff bizStuff, TraceContext traceContext) {

    }
}

class BizStuff {

}


class TraceContext {
    public void add(TraceInfo traceInfo) {
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