import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { AuthService } from '../services/auth';

/**
 * Anexa o token JWT em toda requisição, quando existe sessão ativa.
 * A maioria dos endpoints é pública, então a ausência de token aqui não bloqueia nada;
 * quem decide se recusa a requisição é o backend.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).token;
  if (!token) {
    return next(req);
  }

  return next(req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
};
