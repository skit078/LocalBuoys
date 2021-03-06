package com.thermsx.localbuoys.ui.fragment.info;


import android.app.Fragment;

import com.socks.library.KLog;
import com.thermsx.localbuoys.model.LocationNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class InfoFragmentFactory {

    public static final String METHOD_PREFIX = "isVisibleOn";
    public static final String FRAGMENT_POSTFIX = "Fragment";

    private static String getInfoPackageName() {
        Class<? extends InfoFragment> aClass = InfoFragment.class;
        return aClass.getPackage().getName();
    }

    public static List<Fragment> create(LocationNode node) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fields = getExistingVisibleFields(node);
        for (String field : fields) {
            try {
                fragmentList.add(createFragment(node.getLocationId(), field));
            } catch (ClassNotFoundException e) {
                KLog.e(e);
            }
        }
        return fragmentList;
    }

    private static List<String> getExistingVisibleFields(LocationNode node) throws InvocationTargetException, IllegalAccessException {
        Class<? extends LocationNode> itemClass = node.getClass();
        List<String> fieldNames = new ArrayList<>();
        for (Method method : itemClass.getMethods()) {
            if (method.getGenericReturnType() == boolean.class && method.getName().startsWith(METHOD_PREFIX)) {
                if ((Boolean) method.invoke(node)) {
                    fieldNames.add(method.getName());
                }
            }
        }
        return fieldNames;
    }

    private static <T extends InfoFragment> Fragment createFragment(long itemId, String methodName)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String classPath = methodName.replace(METHOD_PREFIX, getInfoPackageName() + ".").concat(FRAGMENT_POSTFIX);
        Class<? extends InfoFragment> aClass = null;
        try {
            aClass = (Class<? extends InfoFragment>) Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Class " + classPath + " not found");
        }
        return InfoFragment.newInstance(itemId, aClass);
    }
}
