import http from 'k6/http';
import { check, sleep } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { textSummary } from "https://jslib.k6.io/k6-summary/0.0.1/index.js";

// Конфигурация нагрузки: 20 юзеров, 75 секунд
export const options = {
    stages: [
        { duration: '40s', target: 20 },  // Разгон
        { duration: '10s', target: 20 },  // Удержание
        { duration: '20s', target: 0 },   // Спад
    ],
    thresholds: {
        http_req_failed: ['rate<0.10'],   // Ошибок < 10%
        http_req_duration: ['p(90)<2000'], // 90% запросов быстрее 2000мс
    },
};

const SERVER_URL = 'http://localhost:7070/api';

export default function () {
    // Генерируем уникальные имена для каждой итерации каждого пользователя,
    // чтобы избежать ошибок уникальности (Unique Constraint) в базе данных
    const constellationName = `LoadTest-Constellation-${__VU}-${__ITER}`;
    const satelliteName = `Sat-${__VU}-${__ITER}`;

    const jsonHeaders = {
        headers: { 'Content-Type': 'application/json', 'accept': '*/*' },
    };

    // 1. GET: Получение общей сводки системы (Операция чтения)
    let overviewRes = http.get(`${SERVER_URL}/overview`, { headers: { 'accept': '*/*' } });
    check(overviewRes, {
        'GET overview status 200': (r) => r.status === 200,
    });
    sleep(1); // Имитируем паузу реального пользователя

    // 2. POST: Создание новой группировки (Операция записи)
    // Согласно Swagger, имя передается через Query-параметр
    let createRes = http.post(`${SERVER_URL}/constellations?name=${constellationName}`, null, { headers: { 'accept': '*/*' } });
    check(createRes, {
        'POST create constellation status 200': (r) => r.status === 200 || r.status === 201,
    });
    sleep(1);

    // 3. POST: Добавление спутника в созданную группировку (Операция записи/изменения)
    // Структура строго соответствует AddSatelliteRequest из твоего Swagger
    const satellitePayload = JSON.stringify({
        constellationName: constellationName,
        satelliteParam: {
            type: "IMAGE",
            name: satelliteName,
            batteryLevel: 100.0,
            bandwidth: 500.0
        }
    });

    let addSatRes = http.post(`${SERVER_URL}/add-satellites`, satellitePayload, jsonHeaders);
    check(addSatRes, {
        'POST add satellite status 200': (r) => r.status === 200 || r.status === 201,
    });
    sleep(1);
}

// Генерация графического HTML-отчета
export function handleSummary(data) {
    return {
        "load-test-report.html": htmlReport(data),
        stdout: textSummary(data, { indent: " ", enableColors: true }),
    };
}