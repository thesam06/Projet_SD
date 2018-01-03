-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mer 03 Janvier 2018 à 14:49
-- Version du serveur :  10.1.16-MariaDB
-- Version de PHP :  5.6.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


--
-- Base de données :  `projsd`
--

-- --------------------------------------------------------

--
-- Structure de la table `categorie`
--

CREATE TABLE `categorie` (
  `IdCat` int(11) NOT NULL,
  `LibCat` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table permettant de stocker les categories des objets';

--
-- Contenu de la table `categorie`
--

INSERT INTO `categorie` (`IdCat`, `LibCat`) VALUES
(1, 'Livres'),
(2, 'Ordinateurs portables');

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `IdCli` int(11) NOT NULL,
  `Nom` varchar(255) NOT NULL,
  `Prenom` varchar(255) NOT NULL,
  `User` varchar(255) NOT NULL,
  `MotDePasse` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `client`
--

INSERT INTO `client` (`IdCli`, `Nom`, `Prenom`, `User`, `MotDePasse`) VALUES
(1, 'Melvin', 'Moreau', 'alkair83', 'test');

-- --------------------------------------------------------

--
-- Structure de la table `enchere`
--

CREATE TABLE `enchere` (
  `IdEnchere` int(11) NOT NULL,
  `IdObj` int(11) NOT NULL,
  `Offre` int(11) NOT NULL,
  `Encherisseur` int(11) NOT NULL,
  `DateEnchere` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `enchere`
--

INSERT INTO `enchere` (`IdEnchere`, `IdObj`, `Offre`, `Encherisseur`, `DateEnchere`) VALUES
(1, 5, 10, 1, '2018-01-02'),
(2, 5, 13, 1, '2018-01-02');

-- --------------------------------------------------------

--
-- Structure de la table `objet_en_vente`
--

CREATE TABLE `objet_en_vente` (
  `IdObjet` int(11) NOT NULL,
  `Titre` varchar(255) NOT NULL,
  `Description` text,
  `IdCategorie` int(11) DEFAULT NULL,
  `Vendeur` int(11) NOT NULL,
  `Ench` tinyint(1) NOT NULL DEFAULT '0',
  `Prix` int(11) NOT NULL,
  `DateMiseVente` date NOT NULL,
  `DateFinEnchere` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table permettant de stocker les objets en ventes';

--
-- Contenu de la table `objet_en_vente`
--

INSERT INTO `objet_en_vente` (`IdObjet`, `Titre`, `Description`, `IdCategorie`, `Vendeur`, `Ench`, `Prix`, `DateMiseVente`, `DateFinEnchere`) VALUES
(1, 'Honor 5C', 'Telephone', NULL, 0, 0, 0, '0000-00-00', '0000-00-00'),
(3, 'Iphone 8', 'Phone by Apple', NULL, 0, 0, 0, '0000-00-00', '0000-00-00'),
(4, 'HP ENVY', 'Ordinateur portable', 2, 0, 0, 0, '0000-00-00', '0000-00-00'),
(5, 'Hunger Games', 'Le premier tome introduit Katniss Everdeen, une jeune fille de 16 ans originaire du District 12 qui se porte volontaire pour les 74e Hunger Games à la place de sa petite sœur Prim. ', 1, 0, 1, 15, '2018-01-01', '2018-01-16');

--
-- Index pour les tables exportées
--

--
-- Index pour la table `categorie`
--
ALTER TABLE `categorie`
  ADD PRIMARY KEY (`IdCat`);

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`IdCli`);

--
-- Index pour la table `enchere`
--
ALTER TABLE `enchere`
  ADD PRIMARY KEY (`IdEnchere`);

--
-- Index pour la table `objet_en_vente`
--
ALTER TABLE `objet_en_vente`
  ADD PRIMARY KEY (`IdObjet`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `categorie`
--
ALTER TABLE `categorie`
  MODIFY `IdCat` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `IdCli` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT pour la table `enchere`
--
ALTER TABLE `enchere`
  MODIFY `IdEnchere` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT pour la table `objet_en_vente`
--
ALTER TABLE `objet_en_vente`
  MODIFY `IdObjet` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

