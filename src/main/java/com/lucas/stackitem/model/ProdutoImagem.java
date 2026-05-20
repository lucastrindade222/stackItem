package com.lucas.stackitem.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produtos_imagens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProdutoImagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String nomeArquivo; // Guardar o nome ajuda a saber a extensão (ex: .jpg, .png)

    @Lob
    @Column(name = "dados_imagem", columnDefinition = "LONGBLOB", nullable = false)
    private byte[] dadosImagem;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
}
