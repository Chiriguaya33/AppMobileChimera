from fastapi import FastAPI, HTTPException, Form, File, UploadFile
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from typing import Optional
import requests
import os
import shutil
from datetime import datetime

app = FastAPI()

MOODLE_URL = "http://localhost/moodle/webservice/rest/server.php"
TOKEN_ADMIN = "9076f017518e97f488d77d5648672eb2"
UPLOAD_DIR = "tareas_recibidas"

if not os.path.exists(UPLOAD_DIR):
    os.makedirs(UPLOAD_DIR)

# --- LOGIN (Simplificado para tu uso) ---
@app.post("/login")
async def login(request: dict):
    # Lógica de validación con Moodle (ya la tienes)
    return {"id": 3, "fullname": "Steven Armijos", "email": "steven@mail.com", "token": TOKEN_ADMIN}

# --- ENVIAR MISIÓN (FIX ERROR 500 Y MULTI-ENVÍO) ---
@app.post("/enviar_mision")
async def enviar_mision(
    user_id: str = Form(...),
    mission_id: str = Form(...),
    comentario: str = Form(...),
    archivo: Optional[UploadFile] = File(None)
):
    try:
        # 1. Validación de Duplicados: ¿Ya existe una entrega u_m_?
        prefix = f"u{user_id}_m{mission_id}_"
        if any(f.startswith(prefix) for f in os.listdir(UPLOAD_DIR)):
            return JSONResponse(status_code=400, content={"message": "Ya entregaste esta tarea."})

        # 2. Guardar entrega
        if archivo:
            # Guardamos el archivo real (PDF, Imagen, etc.)
            file_path = os.path.join(UPLOAD_DIR, f"{prefix}{archivo.filename}")
            with open(file_path, "wb") as buffer:
                shutil.copyfileobj(archivo.file, buffer)
        else:
            # Si es solo texto, dejamos un archivo .txt como huella
            with open(os.path.join(UPLOAD_DIR, f"{prefix}texto.txt"), "w") as f:
                f.write(comentario)

        return {"status": "success"}
    except Exception as e:
        print(f"Error: {e}")
        raise HTTPException(status_code=500, detail="Fallo en el servidor al procesar el archivo")

# --- SECCIONES (ANIDAMIENTO DE SEMANAS) ---
@app.get("/cursos/{curso_id}/secciones")
async def obtener_secciones_curso(curso_id: int):
    params = {'wstoken': TOKEN_ADMIN, 'wsfunction': 'core_course_get_contents', 'courseid': curso_id, 'moodlewsrestformat': 'json'}
    try:
        response = requests.get(MOODLE_URL, params=params).json()
        final_sections = []
        last_unit = None
        for s in response:
            nombre = s.get('name', '').lower()
            if any(p in nombre for p in ["unidad", "presentac", "proyect"]):
                last_unit = {"id": s['id'], "name": s['name'], "summary": s.get('summary', ''), "modules": s.get('modules', [])}
                final_sections.append(last_unit)
            elif "semana" in nombre and last_unit:
                # Marcamos la semana como una carpeta (sub-section)
                last_unit['modules'].append({"id": s['id'], "name": s['name'], "modname": "sub-section", "instance": s['id']})
        return final_sections
    except: return []

# --- DETALLE DE SEMANA ---
@app.get("/seccion/{section_id}")
async def obtener_detalle_seccion(section_id: int):
    # En un entorno real buscarías los módulos de esta sección en el curso de Moodle
    # Por ahora devolvemos un objeto Section para que Android lo pinte
    return {"id": section_id, "name": "Contenido de la Semana", "modules": []}
