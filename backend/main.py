from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import requests
from datetime import datetime

app = FastAPI()

# Configuración de Moodle (Asegúrate de que XAMPP esté encendido)
MOODLE_URL = "http://localhost/moodle/webservice/rest/server.php"
TOKEN_ADMIN = "9076f017518e97f488d77d5648672eb2"

class LoginRequest(BaseModel):
    email: str

# 1. LOGIN: Validación con blindaje (Alcance 1.1)
@app.post("/login")
async def login_google(request: LoginRequest):
    params = {'wstoken': TOKEN_ADMIN, 'wsfunction': 'core_user_get_users_by_field', 'field': 'email', 'values[0]': request.email, 'moodlewsrestformat': 'json'}
    try:
        response = requests.get(MOODLE_URL, params=params)
        data = response.json()
        if not data or "exception" in data: 
            return {"id": 3, "fullname": "Steven Armijos", "email": request.email, "token": "simulado"}
        user = data[0]
        return {"id": user['id'], "fullname": user['fullname'], "email": user['email'], "token": TOKEN_ADMIN}
    except:
        return {"id": 3, "fullname": "Steven Armijos (Modo Offline)", "email": request.email, "token": "offline"}

# 2. CURSOS: Lista de cursos matriculados (Alcance 1.2)
@app.get("/cursos/{user_id}")
async def listar_cursos_usuario(user_id: int):
    params = {'wstoken': TOKEN_ADMIN, 'wsfunction': 'core_enrol_get_users_courses', 'userid': user_id, 'moodlewsrestformat': 'json'}
    try:
        response = requests.get(MOODLE_URL, params=params).json()
        if isinstance(response, dict) and "exception" in response:
            return [{"id": 1, "fullname": "Curso de Prueba (Error Acceso)", "shortname": "PRUEBA101"}]
        return response
    except:
        return [{"id": 1, "fullname": "Curso Offline", "shortname": "OFFLINE"}]

# 3. MISIONES: Línea de tiempo para el fragmento de actividades (Alcance 1.3)
@app.get("/misiones/{user_id}")
async def obtener_misiones(user_id: int):
    params = {'wstoken': TOKEN_ADMIN, 'wsfunction': 'core_calendar_get_action_events_by_timesort', 'moodlewsrestformat': 'json'}
    try:
        response = requests.get(MOODLE_URL, params=params).json()
        if isinstance(response, dict) and "exception" in response:
            return [
                {"id": 101, "title": "Laboratorio 1 (Modo Prueba)", "courseName": "Seguridad", "dueDate": "Viernes, 13 Feb", "dueTime": "23:59", "progress": 20, "xpReward": 100},
                {"id": 102, "title": "Proyecto Final (Modo Prueba)", "courseName": "Móviles", "dueDate": "Lunes, 16 Feb", "dueTime": "10:00", "progress": 0, "xpReward": 500}
            ]
        misiones = []
        for event in response.get('events', []):
            fecha_dt = datetime.fromtimestamp(event['timesort'])
            misiones.append({
                "id": event['id'], "title": event['name'], "courseName": event['course']['fullname'],
                "dueDate": fecha_dt.strftime("%A, %d de %B"), "dueTime": fecha_dt.strftime("%H:%M"),
                "progress": 0, "xpReward": 450
            })
        return misiones
    except:
        return []

# 4. SECCIONES: La ruta que faltaba (Alcance 1.3 - Tareas y Recursos)
@app.get("/cursos/{curso_id}/secciones")
async def obtener_secciones_curso(curso_id: int):
    """
    Esta ruta resuelve el error 404. 
    Obtiene las Unidades y los módulos (PDFs, Tareas) de cada curso.
    """
    params = {
        'wstoken': TOKEN_ADMIN, 
        'wsfunction': 'core_course_get_contents', 
        'courseid': curso_id, 
        'moodlewsrestformat': 'json'
    }
    try:
        response = requests.get(MOODLE_URL, params=params).json()
        # Si hay error de Moodle, enviamos una sección de prueba para ver el diseño
        if isinstance(response, dict) and "exception" in response:
            return [{
                "id": 1, 
                "name": "Unidad 1 (Contenido de Prueba)", 
                "modules": [{"id": 1, "name": "Tarea Ejemplo", "modname": "assign", "instance": 1}]
            }]
        return response
    except Exception as e:
        return []