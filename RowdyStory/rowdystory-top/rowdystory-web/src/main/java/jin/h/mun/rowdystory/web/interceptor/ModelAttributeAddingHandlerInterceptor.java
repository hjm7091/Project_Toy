package jin.h.mun.rowdystory.web.interceptor;

import jin.h.mun.rowdystory.web.controller.view.attribute.ModelAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class ModelAttributeAddingHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle( HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler, ModelAndView modelAndView ) throws Exception {

        /*
        정적 파일들에 대한 요청도 postHandle 메서드를 통과하므로
        modelAndView 가 null 이 아닌 경우에만 attributes 를 담아준다.
         */
        if ( modelAndView != null ) {

            String viewName = modelAndView.getViewName();

            ModelMap modelMap = modelAndView.getModelMap();

            log.info( "view name : {}", viewName );

            modelMap.addAllAttributes( ModelAttribute.of( viewName ) );

            log.info( "model map : {}", modelMap );
        }
    }
}
