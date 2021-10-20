package com.design.structual.proxy;

public class ProxySearcher implements Searcher {

    //维持一个对真实主题的引用
    private RealSearcher searcher = new RealSearcher();
    private AccessValidator validator;
    private Logger logger;

    @Override
    public String DoSearch(String userId, String keyword) {
        if (validate(userId)){
            String rs = searcher.DoSearch(userId,keyword);
            this.log(userId);
            return rs;
        }
        return null;
    }

    public boolean validate(String userId){
        validator = new AccessValidator();
        return validator.Validate(userId);
    }

    public void log(String userId){
        logger = new Logger();
        logger.Log(userId);
    }
}
