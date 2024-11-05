package com.innovatech.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    // http://localhost:8090/dashboard/finances/1/2021
    @GetMapping("/finances/{idEntrepreneurship}/{year}")
    public ResponseEntity<Map<String, Object>> getFinancesEntrepeneurshipByYear(@PathVariable Long idEntrepreneurship,
            @PathVariable int year) {
        // Simulate data based on the year
        Map<String, Object> chartData1 = new HashMap<>();
        chartData1.put("labels", new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" });
        if (year == 2021) {
            chartData1.put("data", new int[] { 65, 59, 80, 81, 56, 55, 40, 70, 75, 85, 90, 100 });
        } else if (year == 2022) {
            chartData1.put("data", new int[] { 50, 55, 75, 60, 65, 70, 80, 85, 95, 100, 90, 85 });
        }
        chartData1.put("label", "Ventas por Año " + year);
        chartData1.put("color", "#63c5da");

        // Create product sales chart data
        Map<String, Object> chartData2 = new HashMap<>();
        chartData2.put("labels", 
                new String[] { "Producto A", "Producto B", "Producto C", "Producto D", "Producto E" });
        
        if (year == 2021) {
            chartData2.put("data", new int[] { 300, 500, 400, 350, 250 });
        } else if (year == 2022) {
            chartData2.put("data", new int[] { 320, 480, 410, 360, 270 });
        }
        chartData2.put("label", "Ventas " + year);
        chartData2.put("color", new String[] { "#ff6384", "#36a2eb", "#cc65fe", "#ffce56", "#2ecc71" });

        // Create summary data
        Map<String, Object> summaryData = new HashMap<>();
        summaryData.put("ventasVirtual", 133);
        summaryData.put("totalVentas", 444);
        summaryData.put("enviosCompletados", 123);
        summaryData.put("clientes", 5516);

        // Prepare final response
        Map<String, Object> response = new HashMap<>();
        response.put("chartData1", chartData1);
        response.put("chartData2", chartData2);
        response.put("summary", summaryData);

        // Return response as JSON
        return ResponseEntity.ok(response);
    }

    // http://localhost:8090/dashboard/generalStats/1/2021
    @GetMapping("/generalStats/{idEntrepreneurship}/{year}")
    public ResponseEntity<Map<String, Object>> getGeneralStats(@PathVariable Long idEntrepreneurship,
            @PathVariable int year) {
        // Emprendedores Registrados
        Map<String, Object> chartData1 = new HashMap<>();
        chartData1.put("labels", new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" });
        if (year == 2021) {
            chartData1.put("data", new int[] { 50, 45, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150 });
        } else if (year == 2022) {
            chartData1.put("data", new int[] { 55, 50, 65, 75, 85, 95, 105, 115, 125, 135, 145, 155 });
        }
        chartData1.put("label", "Emprendedores Registrados");
        chartData1.put("color", "#42a5f5");

        // Ventas Totales
        Map<String, Object> chartData2 = new HashMap<>();
        chartData2.put("labels",
                new String[] { "Emprendedor A", "Emprendedor B", "Emprendedor C", "Emprendedor D", "Emprendedor E" });
        if (year == 2021) {
            chartData2.put("data", new int[] { 3000, 5000, 4000, 3500, 2500 });
        } else if (year == 2022) {
            chartData2.put("data", new int[] { 3200, 5200, 4200, 3600, 2600 });
        }
        chartData2.put("label", "Ventas Totales");
        chartData2.put("color", new String[] { "#ff6384", "#36a2eb", "#cc65fe", "#ffce56", "#2ecc71" });

        // Ingresos Totales Generados
        Map<String, Object> chartData3 = new HashMap<>();
        chartData3.put("labels", new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" });
        if (year == 2021) {
            chartData3.put("data",
                    new int[] { 12000, 15000, 16000, 18000, 20000, 22000, 25000, 27000, 30000, 32000, 35000, 38000 });
        } else if (year == 2022) {
            chartData3.put("data",
                    new int[] { 13000, 16000, 17000, 19000, 21000, 23000, 26000, 28000, 31000, 33000, 36000, 39000 });
        }
        chartData3.put("label", "Ingresos Totales Generados");
        chartData3.put("color", "#63c5da");

        // Suscripciones Activas
        Map<String, Object> chartData4 = new HashMap<>();
        chartData4.put("labels", new String[] { "Suscripción Básica", "Suscripción Premium", "Suscripción VIP" });
        if (year == 2021) {
            chartData4.put("data", new int[] { 400, 300, 100 });
        } else if (year == 2022) {
            chartData4.put("data", new int[] { 450, 350, 150 });
        }
        chartData4.put("label", "Suscripciones Activas");
        chartData4.put("color", new String[] { "#f39c12", "#e74c3c", "#3498db" });

        // Participación en Eventos
        Map<String, Object> chartData5 = new HashMap<>();
        chartData5.put("labels", new String[] { "Evento 1", "Evento 2", "Evento 3", "Evento 4", "Evento 5" });
        if (year == 2021) {
            chartData5.put("data", new int[] { 200, 180, 220, 160, 250 });
        } else if (year == 2022) {
            chartData5.put("data", new int[] { 210, 190, 230, 170, 260 });
        }
        chartData5.put("label", "Participación en Eventos");
        chartData5.put("color", "#8e44ad");

        // Preparar la respuesta final
        Map<String, Object> response = new HashMap<>();
        response.put("chartData1", chartData1);
        response.put("chartData2", chartData2);
        response.put("chartData3", chartData3);
        response.put("chartData4", chartData4);
        response.put("chartData5", chartData5);

        // Devolver la respuesta en formato JSON
        return ResponseEntity.ok(response);
    }

}

