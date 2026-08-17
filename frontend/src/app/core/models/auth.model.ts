export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
}

export interface CadastroRequest {
  email: string;
  nome: string;
  senha: string;
}

export interface UsuarioResponse {
  email: string;
  nome: string;
}
