package com.literalura;

import com.literalura.model.Autor;
import com.literalura.model.Livro;
import com.literalura.model.RespostaApi;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LivroRepository;
import com.literalura.service.ConsumoApi;
import com.literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://gutendex.com/books/?search=";

    @Override
    public void run(String... args) throws Exception {
        exibeMenu();
    }

    private void exibeMenu() {
        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                    
                    ╔════════════════════════════════════════╗
                    ║       CATÁLOGO DE LIVROS - LITERALURA  ║
                    ╚════════════════════════════════════════╝
                    
                    1 - Buscar livro por título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em determinado ano
                    5 - Listar livros por idioma
                    
                    0 - Sair
                    
                    ╚════════════════════════════════════════╝
                    Escolha uma opção: 
                    """;

            System.out.print(menu);

            try {
                opcao = leitura.nextInt();
                leitura.nextLine();

                switch (opcao) {
                    case 1:
                        buscarLivroPorTitulo();
                        break;
                    case 2:
                        listarLivrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEmAno();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("\n👋 Encerrando aplicação... Até logo!");
                        break;
                    default:
                        System.out.println("\n❌ Opção inválida! Por favor, escolha uma opção válida.");
                }

            } catch (Exception e) {
                System.out.println("\n❌ ERRO: Entrada inválida! Por favor, digite apenas números.");
                leitura.nextLine();
                opcao = -1;
            }
        }

        leitura.close();
    }

    private void buscarLivroPorTitulo() {
        System.out.println("\n📚 Digite o título do livro que deseja buscar:");
        var tituloLivro = leitura.nextLine();

        if (tituloLivro.trim().isEmpty()) {
            System.out.println("❌ ERRO: O título não pode estar vazio!");
            return;
        }

        try {
            var json = consumoApi.obterDados(ENDERECO + tituloLivro.replace(" ", "%20"));

            if (json == null || json.isEmpty()) {
                System.out.println("❌ ERRO: Não foi possível obter dados da API.");
                return;
            }

            RespostaApi resposta = conversor.obterDados(json, RespostaApi.class);

            if (resposta.getResultados() == null || resposta.getResultados().isEmpty()) {
                System.out.println("❌ Nenhum livro encontrado com o título: " + tituloLivro);
                return;
            }

            // Pegar apenas o primeiro resultado
            Livro livroApi = resposta.getResultados().get(0);

            // Configurar autor e idioma (apenas primeiro de cada)
            livroApi.setAutoresFromApi(livroApi.getAutor() != null ? List.of(livroApi.getAutor()) : null);
            livroApi.setIdiomasFromApi(livroApi.getIdioma() != null ? List.of(livroApi.getIdioma()) : null);

            // Verificar se já existe no banco
            Optional<Livro> livroExistente = livroRepository.findByTitulo(livroApi.getTitulo());

            if (livroExistente.isPresent()) {
                System.out.println("\n⚠️ Este livro já está registrado no banco de dados!");
                exibirDadosLivro(livroExistente.get());
            } else {
                // Verificar se o autor já existe
                Autor autor = livroApi.getAutor();
                if (autor != null && autor.getNome() != null) {
                    Optional<Autor> autorExistente = autorRepository.findByNome(autor.getNome());
                    if (autorExistente.isPresent()) {
                        livroApi.setAutor(autorExistente.get());
                    }
                }

                // Salvar no banco
                Livro livroSalvo = livroRepository.save(livroApi);
                System.out.println("\n✅ Livro registrado com sucesso!");
                exibirDadosLivro(livroSalvo);
            }

        } catch (Exception e) {
            System.out.println("❌ ERRO ao buscar livro: " + e.getMessage());
        }
    }

    private void listarLivrosRegistrados() {
        System.out.println("\n📚 LISTA DE LIVROS REGISTRADOS");
        System.out.println("═".repeat(60));

        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("❌ Nenhum livro registrado no banco de dados.");
            System.out.println("💡 Use a opção 1 para buscar e registrar livros.");
        } else {
            System.out.println("Total de livros: " + livros.size() + "\n");
            livros.forEach(this::exibirDadosLivro);
        }
    }

    private void listarAutoresRegistrados() {
        System.out.println("\n👤 LISTA DE AUTORES REGISTRADOS");
        System.out.println("═".repeat(60));

        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("❌ Nenhum autor registrado no banco de dados.");
        } else {
            System.out.println("Total de autores: " + autores.size() + "\n");
            autores.forEach(this::exibirDadosAutor);
        }
    }

    private void listarAutoresVivosEmAno() {
        System.out.println("\n📅 CONSULTAR AUTORES VIVOS EM DETERMINADO ANO");
        System.out.println("═".repeat(60));
        System.out.print("Digite o ano (exemplo: 1900): ");

        try {
            String entrada = leitura.nextLine().trim();

            // Validação: Verificar se é vazio
            if (entrada.isEmpty()) {
                System.out.println("❌ ERRO: O ano não pode estar vazio!");
                return;
            }

            // Validação: Verificar se é número
            Integer ano;
            try {
                ano = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("❌ ERRO: '" + entrada + "' não é um ano válido!");
                System.out.println("💡 Digite apenas números (exemplo: 1850, 1900, 2000)");
                return;
            }

            // Validação: Verificar se o ano é razoável
            if (ano < 1) {
                System.out.println("❌ ERRO: O ano deve ser maior que 0!");
                return;
            }

            if (ano > 2025) {
                System.out.println("⚠️ AVISO: O ano " + ano + " está no futuro!");
                System.out.print("Deseja continuar mesmo assim? (s/n): ");
                String resposta = leitura.nextLine().toLowerCase().trim();
                if (!resposta.equals("s") && !resposta.equals("sim")) {
                    System.out.println("Operação cancelada.");
                    return;
                }
            }

            // Usar a Derived Query do repository
            List<Autor> autoresVivos = autorRepository.findAutoresVivosEmAno(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("\n❌ Nenhum autor vivo encontrado no ano " + ano);
                System.out.println("💡 Use a opção 1 para buscar e registrar mais livros/autores.");
            } else {
                System.out.println("\n✅ Autores vivos em " + ano + ":");
                System.out.println("═".repeat(60));
                System.out.println("📊 Total de autores encontrados: " + autoresVivos.size());
                System.out.println("═".repeat(60));

                autoresVivos.forEach(this::exibirDadosAutor);

                // Estatística adicional
                long autoresAindaVivos = autoresVivos.stream()
                        .filter(a -> a.getAnoFalecimento() == null)
                        .count();

                if (autoresAindaVivos > 0) {
                    System.out.println("ℹ️ Destes, " + autoresAindaVivos + " autor(es) ainda está(ão) vivo(s) atualmente.");
                }
            }

        } catch (Exception e) {
            System.out.println("❌ ERRO inesperado: " + e.getMessage());
            leitura.nextLine(); // Limpar buffer
        }
    }

    private void listarLivrosPorIdioma() {
        var menuIdioma = """
                
                📖 Digite o idioma para buscar livros:
                
                pt - Português
                en - Inglês
                es - Espanhol
                fr - Francês
                
                Idioma: 
                """;

        System.out.print(menuIdioma);
        var idioma = leitura.nextLine().toLowerCase().trim();

        if (idioma.isEmpty()) {
            System.out.println("❌ ERRO: O idioma não pode estar vazio!");
            return;
        }

        List<Livro> livros = livroRepository.findByIdioma(idioma);

        if (livros.isEmpty()) {
            System.out.println("❌ Nenhum livro encontrado no idioma: " + idioma);
            System.out.println("💡 Use a opção 1 para buscar e registrar livros.");
        } else {
            // ESTATÍSTICAS usando Streams
            System.out.println("\n📊 ESTATÍSTICAS DE LIVROS NO IDIOMA '" + idioma.toUpperCase() + "'");
            System.out.println("═".repeat(60));

            // Total de livros
            long totalLivros = livros.size();
            System.out.println("📚 Total de livros: " + totalLivros);

            // Total de downloads (usando Streams)
            long totalDownloads = livros.stream()
                    .filter(l -> l.getNumeroDownloads() != null)
                    .mapToLong(Livro::getNumeroDownloads)
                    .sum();
            System.out.println("📥 Total de downloads: " + totalDownloads);

            // Média de downloads
            double mediaDownloads = livros.stream()
                    .filter(l -> l.getNumeroDownloads() != null)
                    .mapToInt(Livro::getNumeroDownloads)
                    .average()
                    .orElse(0.0);
            System.out.println("📊 Média de downloads: " + String.format("%.2f", mediaDownloads));

            // Livro mais baixado
            livros.stream()
                    .filter(l -> l.getNumeroDownloads() != null)
                    .max((l1, l2) -> l1.getNumeroDownloads().compareTo(l2.getNumeroDownloads()))
                    .ifPresent(l -> System.out.println("🏆 Livro mais baixado: " + l.getTitulo() +
                            " (" + l.getNumeroDownloads() + " downloads)"));

            // Número de autores únicos
            long totalAutores = livros.stream()
                    .map(Livro::getAutor)
                    .filter(a -> a != null)
                    .map(Autor::getNome)
                    .distinct()
                    .count();
            System.out.println("👤 Total de autores: " + totalAutores);

            System.out.println("═".repeat(60));

            // Lista completa de livros
            System.out.println("\n📚 LISTA COMPLETA:");
            System.out.println("═".repeat(60));
            livros.forEach(this::exibirDadosLivro);
        }
    }

    private void exibirDadosLivro(Livro livro) {
        System.out.println("═".repeat(60));
        System.out.println("📖 Título: " + livro.getTitulo());

        if (livro.getAutor() != null) {
            System.out.println("👤 Autor: " + livro.getAutor().getNome());
        }

        if (livro.getIdioma() != null) {
            System.out.println("🌐 Idioma: " + livro.getIdioma());
        }

        if (livro.getNumeroDownloads() != null) {
            System.out.println("📥 Downloads: " + livro.getNumeroDownloads());
        }

        System.out.println("═".repeat(60) + "\n");
    }

    private void exibirDadosAutor(Autor autor) {
        System.out.println("═".repeat(60));
        System.out.println("👤 Nome: " + autor.getNome());
        System.out.println("📅 Ano de Nascimento: " + (autor.getAnoNascimento() != null ? autor.getAnoNascimento() : "N/A"));
        System.out.println("⚰️ Ano de Falecimento: " + (autor.getAnoFalecimento() != null ? autor.getAnoFalecimento() : "Vivo"));
        System.out.println("═".repeat(60) + "\n");
    }
}