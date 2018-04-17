#!/bin/bash
/usr/lib/sqoop/bin/sqoop export --connect jdbc:mysql://mysql:3306/jhipster \
--table usoestacion --export-dir /user/hive/warehouse/biziapp.db/usoestaciones \
--username root --password 1234 --direct --columns \
"nombre_completo, id_estacion, nombre_estacion, fecha_de_uso, intervalo_de_tiempo, devolucion_total, devolucion_media, retiradas_total, retiradas_media, neto, total, fecha_obtencion_datos, fichero_csv, fichero_xls, hashcode"
