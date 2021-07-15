package jin.h.mun.rowdystory.web.controller.view.attribute;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Slf4j
public class ModelAttribute {

    protected String viewName;

    protected Map<String, Object> attributes = new HashMap<>();

    private static final Map<String, ModelAttribute> attributeMap = new HashMap<>();

    private static final String packageName = "jin.h.mun.rowdystory.web.controller.view";

    static {
        Reflections reflections = new Reflections( packageName );
        Set<Class<? extends ModelAttribute>> subTypesOf = reflections.getSubTypesOf( ModelAttribute.class );
        for ( Class<? extends ModelAttribute> clazz : subTypesOf ) {
            try {
                ModelAttribute modelAttribute = clazz.newInstance();
                if ( modelAttribute.getViewName() != null ) {
                    attributeMap.put( modelAttribute.getViewName(), modelAttribute );
                }
            } catch ( InstantiationException | IllegalAccessException e ) {
                throw new RuntimeException( e );
            }
        }

        log.info( "attribute map size : {}", attributeMap.size() );
    }

    public static Map<String, Object> of( String viewName ) {
        return attributeMap.get( viewName ).getAttributes();
    }
}
