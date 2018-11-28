package com.lincs.mobcare.utils;

/**
 * Created by LINCS on 06/03/2017.
 */

public class BasicTypes {
    public class Administrador {
        private int id;
        private String login;
        private String senha;
        private String email;

        public Administrador(int id, String login, String senha, String email) {
            this.id = id;
            this.login = login;
            this.senha = senha;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public class Profissional {
        private int id;
        private String email;
        private String senha;
        private String nome;
        private String especialidade;

        public Profissional(int id, String email, String senha, String nome, String especialidade) {
            this.id = id;
            this.email = email;
            this.senha = senha;
            this.nome = nome;
            this.especialidade = especialidade;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEspecialidade() {
            return especialidade;
        }

        public void setEspecialidade(String especialidade) {
            this.especialidade = especialidade;
        }
    }

    public class Anjo {
        private int id;
        private String nome;
        private String sexo;
        private String dataNascimento;

        private int idAcompanhante;

        public Anjo(int id, String nome, String sexo, String dataNascimento, int idAcompanhante) {
            this.id = id;
            this.nome = nome;
            this.sexo = sexo;
            this.dataNascimento = dataNascimento;
            this.idAcompanhante = idAcompanhante;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getSexo() {
            return sexo;
        }

        public void setSexo(String sexo) {
            this.sexo = sexo;
        }

        public String getDataNascimento() {
            return dataNascimento;
        }

        public void setDataNascimento(String dataNascimento) {
            this.dataNascimento = dataNascimento;
        }

        public int getIdAcompanhante() {
            return idAcompanhante;
        }

        public void setIdAcompanhante(int idAcompanhante) {
            this.idAcompanhante = idAcompanhante;
        }
    }

    public class Acompanhante {
        private int id;
        private String nome;
        private String telefone;
        private String parentesco;
        private String uidFoto;

        public Acompanhante(int id, String nome, String telefone, String parentesco, String uidFoto) {
            this.id = id;
            this.nome = nome;
            this.telefone = telefone;
            this.parentesco = parentesco;
            this.uidFoto = uidFoto;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public String getParentesco() {
            return parentesco;
        }

        public void setParentesco(String parentesco) {
            this.parentesco = parentesco;
        }

        public String getUidFoto() {
            return uidFoto;
        }

        public void setUidFoto(String uidFoto) {
            this.uidFoto = uidFoto;
        }
    }

    public class Aviso {
        private int id;
        private String tipo;
        private String mensagem;

        private int idAdministrador;

        public Aviso(int id, String tipo, String mensagem, int idAdministrador) {
            this.id = id;
            this.tipo = tipo;
            this.mensagem = mensagem;
            this.idAdministrador = idAdministrador;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getMensagem() {
            return mensagem;
        }

        public void setMensagem(String mensagem) {
            this.mensagem = mensagem;
        }

        public int getIdAdministrador() {
            return idAdministrador;
        }

        public void setIdAdministrador(int idAdministrador) {
            this.idAdministrador = idAdministrador;
        }
    }

    public class Video {
        private int id;
        private String titulo;
        private String categoria;
        private String uidArquivo;

        private int idExercicio;

        public Video(int id, String titulo, String categoria, String uidArquivo, int idExercicio) {
            this.id = id;
            this.titulo = titulo;
            this.categoria = categoria;
            this.uidArquivo = uidArquivo;
            this.idExercicio = idExercicio;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public String getUidArquivo() {
            return uidArquivo;
        }

        public void setUidArquivo(String uidArquivo) {
            this.uidArquivo = uidArquivo;
        }

        public int getIdExercicio() {
            return idExercicio;
        }

        public void setIdExercicio(int idExercicio) {
            this.idExercicio = idExercicio;
        }
    }

    public class Exercicio {
        private int id;
        private String titulo;
        private String objetivo;
        private String especialidade;

        public Exercicio(int id, String titulo, String objetivo, String especialidade) {
            this.id = id;
            this.titulo = titulo;
            this.objetivo = objetivo;
            this.especialidade = especialidade;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getObjetivo() {
            return objetivo;
        }

        public void setObjetivo(String objetivo) {
            this.objetivo = objetivo;
        }

        public String getEspecialidade() {
            return especialidade;
        }

        public void setEspecialidade(String especialidade) {
            this.especialidade = especialidade;
        }
    }

    public class Sintoma {
        private int id;
        private String nome;
        private String cor;
        private String data;
        private String uidImagem;

        public Sintoma(int id, String nome, String cor, String data, String uidImagem) {
            this.id = id;
            this.nome = nome;
            this.cor = cor;
            this.data = data;
            this.uidImagem = uidImagem;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getCor() {
            return cor;
        }

        public void setCor(String cor) {
            this.cor = cor;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getUidImagem() {
            return uidImagem;
        }

        public void setUidImagem(String uidImagem) {
            this.uidImagem = uidImagem;
        }
    }

    public class Consulta {
        private int id;
        private String status;
        private String local;
        private String data;
        private String observacao;

        private int idProfissional;
        private int idAdministrador;
        private int idAnjo;

        public Consulta(int id, String status, String local, String data, String observacao, int idProfissional, int idAdministrador, int idAnjo) {
            this.id = id;
            this.status = status;
            this.local = local;
            this.data = data;
            this.observacao = observacao;
            this.idProfissional = idProfissional;
            this.idAdministrador = idAdministrador;
            this.idAnjo = idAnjo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getObservacao() {
            return observacao;
        }

        public void setObservacao(String observacao) {
            this.observacao = observacao;
        }

        public int getIdProfissional() {
            return idProfissional;
        }

        public void setIdProfissional(int idProfissional) {
            this.idProfissional = idProfissional;
        }

        public int getIdAdministrador() {
            return idAdministrador;
        }

        public void setIdAdministrador(int idAdministrador) {
            this.idAdministrador = idAdministrador;
        }

        public int getIdAnjo() {
            return idAnjo;
        }

        public void setIdAnjo(int idAnjo) {
            this.idAnjo = idAnjo;
        }
    }

    public class Realiza {
        private int id;
        private String comentario;
        private String data;
        private String status;

        private String periodicidade;

        private int idProfissional;
        private int idExercicio;
        private int idAnjo;

        public Realiza(int id, String comentario, String data, String status, String periodicidade, int idProfissional, int idExercicio, int idAnjo) {
            this.id = id;
            this.comentario = comentario;
            this.data = data;
            this.status = status;
            this.periodicidade = periodicidade;
            this.idProfissional = idProfissional;
            this.idExercicio = idExercicio;
            this.idAnjo = idAnjo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getComentario() {
            return comentario;
        }

        public void setComentario(String comentario) {
            this.comentario = comentario;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPeriodicidade() {
            return periodicidade;
        }

        public void setPeriodicidade(String periodicidade) {
            this.periodicidade = periodicidade;
        }

        public int getIdProfissional() {
            return idProfissional;
        }

        public void setIdProfissional(int idProfissional) {
            this.idProfissional = idProfissional;
        }

        public int getIdExercicio() {
            return idExercicio;
        }

        public void setIdExercicio(int idExercicio) {
            this.idExercicio = idExercicio;
        }

        public int getIdAnjo() {
            return idAnjo;
        }

        public void setIdAnjo(int idAnjo) {
            this.idAnjo = idAnjo;
        }
    }

    public class Apresenta {
        private int id;

        private int idAnjo;
        private int idSintoma;

        public Apresenta(int id, int idAnjo, int idSintoma) {
            this.id = id;
            this.idAnjo = idAnjo;
            this.idSintoma = idSintoma;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIdAnjo() {
            return idAnjo;
        }

        public void setIdAnjo(int idAnjo) {
            this.idAnjo = idAnjo;
        }

        public int getIdSintoma() {
            return idSintoma;
        }

        public void setIdSintoma(int idSintoma) {
            this.idSintoma = idSintoma;
        }
    }
}
