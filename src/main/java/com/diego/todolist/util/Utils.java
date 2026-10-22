package com.diego.todolist.util;

import java.beans.PropertyDescriptor; // Obtem info das proprs de uma classe (nomes e métodos)
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;

/* Copia as propriedades de um objeto para outro, ignorando os atributos que possuem valor null no objeto de origem. */
public class Utils {

    /*
    source: objeto de origem, de onde os valores serão copiados.
    target: objeto de destino, que receberá os valores. */
    public static void copyNonNullProperties(Object source, Object target) {

        // informa quais propriedades devem ser ignoradas durante a cópia.
        var mustBeIgnored = getNullPropertyNames(source);

        BeanUtils.copyProperties(source, target, mustBeIgnored);
    }
    

    private static String[] getNullPropertyNames(Object source) {

        // O BeanWrapper é um obj que inspeciona as proprs de um objeto. Ele consegue os valores das proprs pelo nome (sem chamar os getters)
        final BeanWrapper sourceWithBeanWrapper = new BeanWrapperImpl(source);


        // O PropertyDescriptor obtém os descritores das propriedades que o BeanWrapper reconhece no objeto. Cada PropertyDescriptor representa informações sobre uma propriedade, incluindo seu nome e os métodos de acesso correspondentes.
        PropertyDescriptor[] propertyDescriptors = sourceWithBeanWrapper.getPropertyDescriptors();

        // Conjunto para guardar o nome das proprs com valor nulo
        Set<String> nullValuePropertyNames = new HashSet<>();

        for (PropertyDescriptor descriptor: propertyDescriptors) {

            // Obtém o nome da propriedade atual
            var propertyName = descriptor.getName();

            // Obtém o valor da propriedade atual
            Object propertyValue = sourceWithBeanWrapper.getPropertyValue(propertyName);

            if(propertyValue == null) {
                nullValuePropertyNames.add(propertyName);
            }
        }

        String[] emptyStringArr = new String[nullValuePropertyNames.size()];

        // Transforma o conjunto de strings em um array de strings
        var nullValuePropertyNamesArr = nullValuePropertyNames.toArray(emptyStringArr);

        return nullValuePropertyNamesArr;
    }
}
