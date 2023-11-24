-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : mar. 21 nov. 2023 à 09:00
-- Version du serveur : 8.0.30
-- Version de PHP : 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `alimenations
--

-- --------------------------------------------------------

--
-- Structure de la table `aliments`
--

CREATE TABLE `aliments` (
  `id` int NOT NULL,
  `nom` varchar(255) NOT NULL,
  `poids_moyen` float NOT NULL,
  `calories` int NOT NULL,
  `vitamines_C` float NOT NULL,
  `type_id` int DEFAULT NULL,
  `couleur_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `aliments`
--

INSERT INTO `aliments` (`id`, `nom`, `poids_moyen`, `calories`, `vitamines_C`, `type_id`, `couleur_id`) VALUES
(1, 'Bigaradier', 0.1, 66, 50.5, 1, 6),
(2, 'Banane', 0.15, 89, 0, 1, 6),
(3, 'Orange', 0.2, 47, 0, 1, 9),
(4, 'Fraise', 0.02, 33, 0, 1, 3),
(5, 'Abricot', 0.05, 48, 0, 1, 2),
(6, 'Ananas', 1, 50, 0, 1, 4),
(7, 'Avocat', 0.2, 160, 0, 1, 13),
(8, 'Cassis', 0.01, 63, 0, 1, 1),
(9, 'Cerise', 0.01, 50, 0, 1, 3),
(10, 'Citron', 0.15, 29, 0, 1, 7),
(11, 'Clémentine', 0.08, 47, 0, 1, 4),
(12, 'Coing', 0.3, 57, 0, 1, 6),
(13, 'Datte', 0.02, 282, 0, 1, 14),
(14, 'Figue', 0.05, 74, 0, 1, 14),
(15, 'Framboise', 0.01, 53, 0, 1, 3),
(16, 'Grenade', 0.2, 83, 0, 1, 3),
(17, 'Groseille', 0.01, 56, 0, 1, 3),
(18, 'Kiwi', 0.07, 61, 0, 1, 13),
(19, 'Litchi', 0.02, 66, 0, 1, 5),
(20, 'Mandarine', 0.08, 53, 0, 1, 7),
(21, 'Mangue', 0.3, 60, 0, 1, 8),
(22, 'Melon', 1, 34, 0, 1, 13),
(23, 'Mirabelle', 0.02, 67, 0, 1, 6),
(24, 'Mûre', 0.01, 43, 0, 1, 1),
(25, 'Myrtille', 0.01, 57, 0, 1, 5),
(26, 'Nectarine', 0.1, 44, 0, 1, 21),
(27, 'Noix de coco', 1, 354, 0, 1, 11),
(28, 'Olive', 0.02, 115, 0, 1, 13),
(29, 'Pamplemousse', 0.3, 42, 0, 1, 6),
(30, 'Papaye', 1, 43, 0, 1, 4),
(31, 'Pastèque', 2, 30, 0, 1, 13),
(32, 'Pêche', 0.15, 39, 0, 1, 4),
(33, 'Poire', 0.2, 57, 0, 1, 13),
(34, 'Pomelo', 0.25, 38, 0, 1, 6),
(35, 'Prune', 0.04, 46, 0, 1, 14),
(36, 'Raisin', 0.02, 0, 0, 1, 14),
(64, 'Carotte', 0.1, 41, 0, 4, 5),
(65, 'Brocoli', 0.3, 34, 0, 4, 5),
(66, 'Pomme de terre', 0.2, 0, 0, 4, 11),
(67, 'Tomate', 0.15, 0, 0, 4, 8),
(68, 'Artichaut', 0.5, 47, 0, 4, 1),
(69, 'Asperge', 0.1, 20, 0, 4, 45),
(70, 'Aubergine', 0.2, 25, 0, 4, 6),
(71, 'Betterave', 0.3, 43, 0, 4, 4),
(72, 'Blette', 0.3, 19, 0, 4, 6),
(73, 'Brocoli', 0.3, 34, 0, 4, 8),
(74, 'Carotte', 0.1, 41, 0, 4, 22),
(75, 'Céleri', 0.4, 16, 0, 4, 6),
(76, 'Champignon', 0.05, 22, 0, 4, 2),
(77, 'Chou-fleur', 1, 25, 0, 4, 1),
(78, 'Chou de Bruxelles', 0.02, 43, 0, 4, 16),
(79, 'Concombre', 0.2, 15, 0, 4, 20),
(80, 'Courgette', 0.3, 17, 0, 4, 12),
(81, 'Échalote', 0.05, 72, 0, 4, 5),
(82, 'Endive', 0.2, 17, 0, 4, 16),
(83, 'Épinard', 0.2, 23, 0, 4, 12),
(84, 'Fenouil', 0.4, 31, 0, 4, 4),
(85, 'Haricot vert', 0.05, 31, 0, 4, 5),
(86, 'Laitue', 0.3, 15, 0, 4, 5),
(87, 'Maïs', 1, 86, 0, 4, 21),
(88, 'Navet', 0.2, 28, 0, 4, 8),
(89, 'Oignon', 0.1, 40, 0, 4, 1),
(90, 'Panais', 0.3, 75, 0, 4, 18),
(91, 'Petit pois', 0.01, 81, 0, 4, 21),
(92, 'Poireau', 0.3, 0, 0, 4, 9),
(93, 'Poivron', 0.2, 0, 0, 4, 12),
(94, 'Pomme de terre', 0.2, 0, 0, 4, 6),
(95, 'Potiron', 2, 0, 0, 4, 5),
(96, 'Radis', 0.02, 0, 0, 4, 20),
(97, 'Tomate', 0.15, 0, 0, 4, 22),
(127, 'Riz', 0.001, 1, 0, 2, 2),
(129, 'Litchi Mur', 0.02, 66, 0, 1, 6),
(130, 'Litchi Mur', 0.02, 66, 0, 1, 6),
(131, 'Litchi Mur', 0.02, 66, 0, 1, 6),
(132, 'Litchi Mur', 0.02, 66, 0, 1, 6),
(133, 'Litchi Mur', 0.02, 66, 0, 1, 6),
(134, 'Litchi pas beau', 0.02, 66, 0, 1, 6),
(135, 'fgdsgdfgdg', 0.02, 5000, 50.5, 2, 2);

-- --------------------------------------------------------

--
-- Structure de la table `couleur`
--

CREATE TABLE `couleur` (
  `id` int NOT NULL,
  `nom` varchar(255) NOT NULL,
  `hexadecimal_rvb` char(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '#FFFFFF'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `couleur`
--

INSERT INTO `couleur` (`id`, `nom`, `hexadecimal_rvb`) VALUES
(1, 'argent', '#C0C0C0'),
(2, 'blanc', '#FFFFFF'),
(3, 'Rouge', '#FF0000'),
(4, 'Citron vert', '#00FF00'),
(5, 'blanc', '#FFFFFF'),
(6, 'Bleu', '#0000FF'),
(7, 'Jaune', '#FFFF00'),
(8, 'Cyan / Aqua', '#00FFFF'),
(9, 'Magenta / Fuchsia', '#FF00FF'),
(11, 'Bordeaux', '#800000'),
(12, 'olive', '#808000'),
(13, 'vert', '#008000'),
(14, 'Violet', '#800080'),
(15, 'Sarcelle', '#008080'),
(16, 'Marine', '#000080'),
(18, 'Red', '#FF0000'),
(20, 'Crème', '#FF5555'),
(21, 'Blanc', '#FFFFFF'),
(22, 'Noir', '#000000'),
(23, 'Orange', '#FF5500'),
(24, 'test2', '#000000'),
(25, 'test3', '#000000'),
(26, 'test4', '#000000'),
(27, 'test4', '#000000'),
(28, 'test4', '#000000'),
(29, 'test4', '#000000'),
(30, 'test4', '#000000'),
(31, 'test6', '#000000'),
(32, 'test6', '#000000'),
(33, 'test6', '#000000'),
(34, 'test6', '#000000'),
(35, 'test8', '#000000'),
(36, 'test9', '#000000'),
(37, 'test9', '#000000'),
(38, 'test9', '#000000'),
(39, 'test9', '#000000'),
(40, 'test10', '#000000'),
(41, 'test10', '#000000'),
(42, 'test10', '#000000'),
(43, 'test10', '#000000'),
(44, 'test10', '#000000'),
(45, 'test10', '#000000'),
(46, 'test11', '#000000'),
(47, 'test12', '#000000'),
(48, 'Blanc Blanc', '#000000'),
(49, 'titi', '#000000'),
(50, 'Dirty', '#CCCCCC');

-- --------------------------------------------------------

--
-- Structure de la table `type_aliment`
--

CREATE TABLE `type_aliment` (
  `id` int NOT NULL,
  `nom` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `type_aliment`
--

INSERT INTO `type_aliment` (`id`, `nom`) VALUES
(1, 'fruit'),
(2, 'céréale'),
(3, 'condiment'),
(4, 'légume');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `aliments`
--
ALTER TABLE `aliments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `type_id` (`type_id`),
  ADD KEY `couleur_id` (`couleur_id`);

--
-- Index pour la table `couleur`
--
ALTER TABLE `couleur`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `type_aliment`
--
ALTER TABLE `type_aliment`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `aliments`
--
ALTER TABLE `aliments`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=136;

--
-- AUTO_INCREMENT pour la table `couleur`
--
ALTER TABLE `couleur`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT pour la table `type_aliment`
--
ALTER TABLE `type_aliment`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `aliments`
--
ALTER TABLE `aliments`
  ADD CONSTRAINT `aliments_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `type_aliment` (`id`),
  ADD CONSTRAINT `aliments_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `type_aliment` (`id`),
  ADD CONSTRAINT `aliments_ibfk_3` FOREIGN KEY (`couleur_id`) REFERENCES `couleur` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
