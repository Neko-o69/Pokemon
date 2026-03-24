-- phpMyAdmin SQL Dump
-- version 5.2.1deb1+deb12u1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : mer. 11 mars 2026 à 12:14
-- Version du serveur : 10.11.14-MariaDB-0+deb12u2
-- Version de PHP : 8.2.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `pokemon`
--

-- --------------------------------------------------------

--
-- Structure de la table `attaques`
--

CREATE TABLE `attaques` (
  `id` int(11) NOT NULL,
  `libelle` varchar(50) NOT NULL,
  `type_id` int(11) NOT NULL,
  `pp` int(11) NOT NULL,
  `puissance` int(11) DEFAULT NULL,
  `precis` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `attaques`
--

INSERT INTO `attaques` (`id`, `libelle`, `type_id`, `pp`, `puissance`, `precis`) VALUES
(1, 'Abîme', 13, 5, NULL, 30),
(2, 'Acid\'armure', 10, 40, NULL, 100),
(3, 'Acide', 10, 30, 40, 100),
(4, 'Adaptation', 8, 40, NULL, 100),
(5, 'Affûtage', 8, 30, NULL, 100),
(6, 'Amnésie', 11, 20, NULL, 100),
(7, 'Armure', 8, 30, NULL, 100),
(8, 'Balayage', 1, 20, 50, 90),
(9, 'Bec Vrille', 15, 20, 80, 100),
(10, 'Bélier', 8, 20, 90, 85),
(11, 'Berceuse', 8, 15, NULL, 55),
(12, 'Blizzard', 6, 5, 120, 90),
(13, 'Bomb\'oeuf', 8, 10, 100, 75),
(14, 'Bouclier', 11, 30, NULL, 100),
(15, 'Boul\'armure', 8, 20, NULL, 100),
(16, 'Brouillard', 8, 20, NULL, 100),
(17, 'Brume', 6, 30, NULL, 100),
(18, 'Buée noire', 6, 30, NULL, 100),
(19, 'Bulles d\'O', 3, 20, 65, 100),
(20, 'Cage éclair', 4, 20, NULL, 100),
(21, 'Cascade', 3, 15, 80, 100),
(22, 'Charge', 8, 35, 35, 85),
(23, 'Choc mental', 11, 25, 50, 100),
(24, 'Claquoir', 3, 10, 35, 75),
(25, 'Clonage', 8, 10, NULL, 100),
(26, 'Combo-griffes', 8, 15, 20, 80),
(27, 'Constriction', 8, 35, 10, 100),
(28, 'Copie', 8, 10, NULL, 100),
(29, 'Coud\'boule', 8, 15, 70, 100),
(30, 'Coud\'krâne', 8, 15, 100, 100),
(31, 'Coupe', 8, 30, 50, 95),
(32, 'Coupe-vent', 8, 10, 80, 75),
(33, 'Croc de mort', 8, 15, 80, 70),
(34, 'Croc fatal', 8, 10, NULL, 90),
(35, 'Croissance', 8, 40, NULL, 100),
(36, 'Cru-aile', 15, 35, 35, 100),
(37, 'Cyclone', 8, 20, NULL, 85),
(38, 'Damoclès', 8, 15, 15, 100),
(39, 'Danse-Flamme', 5, 15, 15, 70),
(40, 'Danse-Fleur', 9, 20, 70, 100),
(41, 'Danse-Lames', 8, 30, NULL, 100),
(42, 'Dard-Nuée', 7, 20, 14, 85),
(43, 'Dard-Venin', 10, 35, 15, 100),
(44, 'Déflagration', 5, 5, 120, 85),
(45, 'Destruction', 8, 5, 260, 100),
(46, 'Détritus', 10, 20, 65, 100),
(47, 'Dévorêve', 11, 15, 100, 100),
(48, 'Double-Dard', 7, 20, 25, 100),
(49, 'Double-Pied', 1, 30, 30, 100),
(50, 'Draco-Rage', 2, 10, 40, 100),
(51, 'Éboulement', 12, 10, 80, 90),
(52, 'Éclair', 4, 30, 40, 100),
(53, 'É-Coque', 8, 10, NULL, 100),
(54, 'Écrasement', 8, 20, 65, 100),
(55, 'Écras\'face', 8, 35, 40, 100),
(56, 'Écume', 3, 30, 20, 100),
(57, 'Empal\'korne', 8, 5, NULL, 30),
(58, 'Entrave', 8, 20, NULL, 55),
(59, 'Étreinte', 8, 20, 15, 75),
(60, 'Explosion', 8, 5, 340, 100),
(61, 'Fatal-Foudre', 4, 10, 120, 70),
(62, 'Flammèches', 5, 25, 40, 100),
(63, 'Flash', 8, 20, NULL, 70),
(64, 'Force', 8, 15, 80, 100),
(65, 'Force-Poigne', 8, 30, 55, 100),
(66, 'Fouet Lianes', 9, 10, 35, 100),
(67, 'Frappe-Atlas', 1, 20, 0, 100),
(68, 'Frénésie', 8, 20, 20, 100),
(69, 'Furie', 8, 20, 15, 85),
(70, 'Gaz Toxik', 10, 40, NULL, 55),
(71, 'Griffe', 8, 35, 40, 100),
(72, 'Grincement', 8, 40, NULL, 85),
(73, 'Grobisou', 8, 10, NULL, 75),
(74, 'Gros\'yeux', 8, 30, NULL, 100),
(75, 'Guillotine', 8, 5, NULL, 30),
(76, 'Hâte', 11, 30, NULL, 100),
(77, 'Hurlement', 8, 20, NULL, 100),
(78, 'Hydrocanon', 3, 5, 120, 80),
(79, 'Hypnose', 11, 20, NULL, 60),
(80, 'Intimidation', 8, 30, NULL, 75),
(81, 'Jackpot', 8, 20, 40, 100),
(82, 'Jet de sable', 8, 15, NULL, 100),
(83, 'Jet de pierres', 12, 15, 50, 75),
(84, 'Koud\'korne', 8, 25, 65, 100),
(85, 'Lance-Flamme', 5, 15, 95, 100),
(86, 'Lance-Soleil', 9, 10, 120, 100),
(87, 'Laser-Glace', 6, 10, 95, 100),
(88, 'Léchouille', 14, 30, 20, 100),
(89, 'Ligotage', 8, 20, 15, 85),
(90, 'Liliput', 8, 20, NULL, 100),
(91, 'Lutte', 8, 1, 100, 100),
(92, 'Mania', 8, 20, 90, 100),
(93, 'Massd\'os', 13, 20, 65, 85),
(94, 'Mawashi Geri', 1, 15, 60, 80),
(95, 'Méga-Sangsue', 9, 10, 40, 100),
(96, 'Météores', 8, 20, 60, 100),
(97, 'Métronome', 8, 10, NULL, 100),
(98, 'Mimique', 15, 20, NULL, 100),
(99, 'Mimi-Queue', 8, 30, NULL, 100),
(100, 'Morphing', 8, 10, NULL, 100),
(101, 'Morsure', 8, 25, 60, 100),
(102, 'Mur Lumière', 11, 30, NULL, 100),
(103, 'Onde Boréale', 6, 20, 65, 100),
(104, 'Onde Folie', 14, 10, NULL, 100),
(105, 'Osmerang', 13, 10, 50, 90),
(106, 'Para-Spore', 9, 30, NULL, 75),
(107, 'Patience', 8, 10, NULL, 100),
(108, 'Picanon', 8, 15, 20, 100),
(109, 'Picpic', 15, 35, 35, 100),
(110, 'Pied Sauté', 1, 25, 70, 95),
(111, 'Pied Voltige', 1, 20, 85, 90),
(112, 'Pilonnage', 8, 20, 15, 85),
(113, 'Pince-Masse', 3, 10, 90, 85),
(114, 'Piqué', 15, 5, 140, 90),
(115, 'Pistolet à O', 3, 20, 40, 100),
(116, 'Plaquage', 8, 15, 85, 100),
(117, 'Poing Comète', 8, 15, 18, 85),
(118, 'Poing de Feu', 5, 15, 75, 100),
(119, 'Poing Karaté', 1, 25, 50, 100),
(120, 'Poing Éclair', 4, 15, 75, 100),
(121, 'Poing Glace', 6, 15, 75, 100),
(122, 'Poudre Dodo', 9, 15, NULL, 75),
(123, 'Poudre Toxik', 10, 35, NULL, 75),
(124, 'Protection', 11, 20, NULL, 100),
(125, 'Psyko', 11, 10, 90, 100),
(126, 'Puissance', 8, 30, NULL, 100),
(127, 'Purédpois', 10, 20, 20, 70),
(128, 'Rafale Psy', 11, 20, 65, 100),
(129, 'Reflet', 8, 15, NULL, 100),
(130, 'Repli', 3, 40, NULL, 100),
(131, 'Repos', 11, 10, NULL, 100),
(132, 'Riposte', 1, 20, NULL, 100),
(133, 'Rugissement', 8, 10, NULL, 100),
(134, 'Sacrifice', 1, 25, 80, 80),
(135, 'Sécrétion', 7, 40, NULL, 95),
(136, 'Séisme', 13, 10, 100, 100),
(137, 'Soin', 8, 10, NULL, 100),
(138, 'Sonicboom', 8, 20, 20, 100),
(139, 'Souplesse', 8, 20, 80, 75),
(140, 'Spore', 9, 15, NULL, 100),
(141, 'Surf', 3, 15, 95, 100),
(142, 'Télékinésie', 11, 15, NULL, 75),
(143, 'Téléport', 11, 20, NULL, 100),
(144, 'Ténèbres', 14, 15, 0, 100),
(145, 'Tonnerre', 4, 15, 95, 100),
(146, 'Torgnoles', 8, 10, 15, 85),
(147, 'Tornade', 15, 35, 40, 100),
(148, 'Toxik', 10, 10, NULL, 85),
(149, 'Tranche', 8, 20, 70, 100),
(150, 'Tranch\'herbe', 9, 25, 55, 95),
(151, 'Trempette', 3, 40, NULL, 100),
(152, 'Triplattaque', 8, 10, 80, 100),
(153, 'Tunnel', 13, 10, 100, 100),
(154, 'Ultimapoing', 8, 20, 80, 85),
(155, 'Ultimawashi', 8, 5, 120, 75),
(156, 'Ultralaser', 8, 5, 120, 90),
(157, 'Ultrason', 8, 20, NULL, 55),
(158, 'Uppercut', 8, 10, 70, 100),
(159, 'Vague Psy', 11, 15, NULL, 80),
(160, 'Vampigraine', 9, 10, NULL, 90),
(161, 'Vampirisme', 7, 15, 20, 100),
(162, 'Vive Attaque', 8, 30, 40, 100),
(163, 'Vol', 15, 15, 70, 95),
(164, 'Vol-vie', 9, 20, 20, 100),
(165, 'Yoga', 11, 40, NULL, 100);

-- --------------------------------------------------------

--
-- Structure de la table `detient_pokemons`
--

CREATE TABLE `detient_pokemons` (
  `dresseur_id` int(11) NOT NULL,
  `pokemon_id` int(11) NOT NULL,
  `niveau` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `detient_pokemons`
--

INSERT INTO `detient_pokemons` (`dresseur_id`, `pokemon_id`, `niveau`) VALUES
(2, 74, 12),
(2, 95, 14),
(3, 120, 18),
(3, 121, 21),
(4, 100, 21),
(4, 25, 18),
(4, 26, 24),
(5, 71, 29),
(5, 114, 24),
(5, 45, 29),
(6, 109, 37),
(6, 89, 39),
(6, 109, 37),
(6, 110, 43),
(7, 64, 38),
(7, 122, 37),
(7, 49, 38),
(7, 65, 43),
(8, 58, 42),
(8, 77, 40),
(8, 78, 42),
(8, 59, 47),
(9, 111, 42),
(9, 51, 42),
(9, 31, 44),
(9, 34, 45),
(9, 112, 50),
(10, 87, 54),
(10, 91, 53),
(10, 80, 54),
(10, 124, 56),
(10, 131, 56),
(11, 95, 53),
(11, 107, 55),
(11, 106, 55),
(11, 95, 56),
(11, 68, 58),
(12, 94, 56),
(12, 42, 56),
(12, 93, 55),
(12, 24, 58),
(12, 94, 60),
(13, 130, 58),
(13, 148, 56),
(13, 148, 56),
(13, 142, 60),
(13, 149, 62);

-- --------------------------------------------------------

--
-- Structure de la table `dresseurs`
--

CREATE TABLE `dresseurs` (
  `id` int(11) NOT NULL,
  `nom` varchar(25) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `dresseurs`
--

INSERT INTO `dresseurs` (`id`, `nom`) VALUES
(2, 'Pierre'),
(3, 'Ondine'),
(4, 'Major Bob'),
(5, 'Erika'),
(6, 'Koga'),
(7, 'Morgane'),
(8, 'Auguste'),
(9, 'Giovanni'),
(10, 'Olga'),
(11, 'Aldo'),
(12, 'Agatha'),
(13, 'Peter');

-- --------------------------------------------------------

--
-- Structure de la table `efficacite`
--

CREATE TABLE `efficacite` (
  `fkAtt` int(11) NOT NULL,
  `fkDef` int(11) NOT NULL,
  `multi` float NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `efficacite`
--

INSERT INTO `efficacite` (`fkAtt`, `fkDef`, `multi`) VALUES
(1, 1, 1),
(1, 2, 1),
(1, 3, 1),
(1, 4, 1),
(1, 5, 1),
(1, 6, 2),
(1, 7, 0.5),
(1, 8, 2),
(1, 9, 1),
(1, 10, 0.5),
(1, 11, 0.5),
(1, 12, 2),
(1, 13, 1),
(1, 14, 0),
(1, 15, 0.5),
(2, 1, 1),
(2, 2, 2),
(2, 3, 1),
(2, 4, 1),
(2, 5, 1),
(2, 6, 1),
(2, 7, 1),
(2, 8, 1),
(2, 9, 1),
(2, 10, 1),
(2, 11, 1),
(2, 12, 1),
(2, 13, 1),
(2, 14, 1),
(2, 15, 1),
(3, 1, 1),
(3, 2, 0.5),
(3, 3, 0.5),
(3, 4, 1),
(3, 5, 2),
(3, 6, 1),
(3, 7, 1),
(3, 8, 1),
(3, 9, 0.5),
(3, 10, 1),
(3, 11, 1),
(3, 12, 2),
(3, 13, 2),
(3, 14, 1),
(3, 15, 1),
(4, 1, 1),
(4, 2, 0.5),
(4, 3, 2),
(4, 4, 0.5),
(4, 5, 1),
(4, 6, 1),
(4, 7, 1),
(4, 8, 1),
(4, 9, 0.5),
(4, 10, 1),
(4, 11, 1),
(4, 12, 1),
(4, 13, 0),
(4, 14, 1),
(4, 15, 2),
(5, 1, 1),
(5, 2, 0.5),
(5, 3, 0.5),
(5, 4, 1),
(5, 5, 0.5),
(5, 6, 2),
(5, 7, 2),
(5, 8, 1),
(5, 9, 2),
(5, 10, 1),
(5, 11, 1),
(5, 12, 0.5),
(5, 13, 1),
(5, 14, 1),
(5, 15, 1),
(6, 1, 1),
(6, 2, 2),
(6, 3, 0.5),
(6, 4, 1),
(6, 5, 1),
(6, 6, 0.5),
(6, 7, 1),
(6, 8, 1),
(6, 9, 2),
(6, 10, 1),
(6, 11, 1),
(6, 12, 1),
(6, 13, 2),
(6, 14, 1),
(6, 15, 2),
(7, 1, 0.5),
(7, 2, 1),
(7, 3, 1),
(7, 4, 1),
(7, 5, 0.5),
(7, 6, 1),
(7, 7, 1),
(7, 8, 1),
(7, 9, 2),
(7, 10, 2),
(7, 11, 2),
(7, 12, 1),
(7, 13, 1),
(7, 14, 1),
(7, 15, 0.5),
(8, 1, 1),
(8, 2, 1),
(8, 3, 1),
(8, 4, 1),
(8, 5, 1),
(8, 6, 1),
(8, 7, 1),
(8, 8, 1),
(8, 9, 1),
(8, 10, 1),
(8, 11, 1),
(8, 12, 0.5),
(8, 13, 1),
(8, 14, 0),
(8, 15, 1),
(9, 1, 1),
(9, 2, 0.5),
(9, 3, 2),
(9, 4, 1),
(9, 5, 0.5),
(9, 6, 1),
(9, 7, 0.5),
(9, 8, 1),
(9, 9, 0.5),
(9, 10, 0.5),
(9, 11, 1),
(9, 12, 2),
(9, 13, 2),
(9, 14, 1),
(9, 15, 0.5),
(10, 1, 1),
(10, 2, 1),
(10, 3, 1),
(10, 4, 1),
(10, 5, 1),
(10, 6, 1),
(10, 7, 2),
(10, 8, 2),
(10, 9, 2),
(10, 10, 0.5),
(10, 11, 1),
(10, 12, 1),
(10, 13, 0.5),
(10, 14, 0),
(10, 15, 0.5),
(11, 1, 2),
(11, 2, 1),
(11, 3, 1),
(11, 4, 1),
(11, 5, 1),
(11, 6, 1),
(11, 7, 1),
(11, 8, 1),
(11, 9, 1),
(11, 10, 2),
(11, 11, 0.5),
(11, 12, 1),
(11, 13, 1),
(11, 14, 0),
(11, 15, 1),
(12, 1, 0.5),
(12, 2, 1),
(12, 3, 1),
(12, 4, 1),
(12, 5, 2),
(12, 6, 2),
(12, 7, 2),
(12, 8, 1),
(12, 9, 1),
(12, 10, 1),
(12, 11, 1),
(12, 12, 1),
(12, 13, 0.5),
(12, 14, 1),
(12, 15, 2),
(13, 1, 1),
(13, 2, 1),
(13, 3, 1),
(13, 4, 2),
(13, 5, 2),
(13, 6, 1),
(13, 7, 0.5),
(13, 8, 1),
(13, 9, 0.5),
(13, 10, 2),
(13, 11, 1),
(13, 12, 2),
(13, 13, 1),
(13, 14, 1),
(13, 15, 0),
(14, 1, 1),
(14, 2, 1),
(14, 3, 1),
(14, 4, 1),
(14, 5, 1),
(14, 6, 1),
(14, 7, 1),
(14, 8, 0),
(14, 9, 1),
(14, 10, 0.5),
(14, 11, 0),
(14, 12, 1),
(14, 13, 1),
(14, 14, 2),
(14, 15, 1),
(15, 1, 2),
(15, 2, 1),
(15, 3, 1),
(15, 4, 0.5),
(15, 5, 1),
(15, 6, 1),
(15, 7, 2),
(15, 8, 1),
(15, 9, 2),
(15, 10, 1),
(15, 11, 1),
(15, 12, 0.5),
(15, 13, 1),
(15, 14, 1),
(15, 15, 1);

-- --------------------------------------------------------

--
-- Structure de la table `est_type`
--

CREATE TABLE `est_type` (
  `pokemon_id` int(11) NOT NULL,
  `type_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `est_type`
--

INSERT INTO `est_type` (`pokemon_id`, `type_id`) VALUES
(1, 9),
(1, 10),
(2, 9),
(2, 10),
(3, 9),
(3, 10),
(4, 5),
(5, 5),
(6, 5),
(6, 15),
(7, 3),
(8, 3),
(9, 3),
(10, 7),
(11, 7),
(12, 7),
(12, 15),
(13, 7),
(13, 10),
(14, 7),
(14, 10),
(15, 7),
(15, 10),
(16, 8),
(16, 15),
(17, 8),
(17, 15),
(18, 8),
(18, 15),
(19, 8),
(20, 8),
(21, 8),
(21, 15),
(22, 8),
(22, 15),
(23, 10),
(24, 10),
(25, 4),
(26, 4),
(27, 13),
(28, 13),
(29, 10),
(30, 10),
(31, 10),
(31, 13),
(32, 10),
(33, 10),
(34, 10),
(34, 13),
(35, 8),
(36, 8),
(37, 5),
(38, 5),
(39, 8),
(40, 8),
(41, 10),
(41, 15),
(42, 10),
(42, 15),
(43, 9),
(43, 10),
(44, 9),
(44, 10),
(45, 9),
(45, 10),
(46, 7),
(46, 9),
(47, 7),
(47, 9),
(48, 7),
(48, 10),
(49, 7),
(49, 10),
(50, 13),
(51, 13),
(52, 8),
(53, 8),
(54, 3),
(55, 3),
(56, 1),
(57, 1),
(58, 5),
(59, 5),
(60, 3),
(61, 3),
(62, 3),
(62, 1),
(63, 11),
(64, 11),
(65, 11),
(66, 1),
(67, 1),
(68, 1),
(69, 9),
(69, 10),
(70, 9),
(70, 10),
(71, 9),
(71, 10),
(72, 3),
(72, 10),
(73, 3),
(73, 10),
(74, 12),
(74, 13),
(75, 12),
(75, 13),
(76, 12),
(76, 13),
(77, 5),
(78, 5),
(79, 3),
(79, 11),
(80, 3),
(80, 11),
(81, 4),
(82, 4),
(83, 8),
(83, 15),
(84, 8),
(84, 15),
(85, 8),
(85, 15),
(86, 3),
(87, 3),
(87, 6),
(88, 10),
(89, 10),
(90, 3),
(91, 3),
(91, 6),
(92, 14),
(92, 10),
(93, 14),
(93, 10),
(94, 14),
(94, 10),
(95, 12),
(95, 13),
(96, 11),
(97, 11),
(98, 3),
(99, 3),
(100, 4),
(101, 4),
(102, 9),
(102, 11),
(103, 9),
(103, 11),
(104, 13),
(105, 13),
(106, 1),
(107, 1),
(108, 8),
(109, 10),
(110, 10),
(111, 13),
(111, 12),
(112, 13),
(112, 12),
(113, 8),
(114, 9),
(115, 8),
(116, 3),
(117, 3),
(118, 3),
(119, 3),
(120, 3),
(121, 3),
(121, 11),
(122, 11),
(123, 7),
(123, 15),
(124, 6),
(124, 11),
(125, 4),
(126, 5),
(127, 7),
(128, 8),
(129, 3),
(130, 3),
(130, 15),
(131, 3),
(131, 6),
(132, 8),
(133, 8),
(134, 3),
(135, 4),
(136, 5),
(137, 8),
(138, 12),
(138, 3),
(139, 12),
(139, 3),
(140, 12),
(140, 3),
(141, 12),
(141, 3),
(142, 12),
(142, 15),
(143, 8),
(144, 6),
(144, 15),
(145, 4),
(145, 15),
(146, 5),
(146, 15),
(147, 2),
(148, 2),
(149, 2),
(149, 15),
(150, 11),
(151, 11);

-- --------------------------------------------------------

--
-- Structure de la table `evolue_en`
--

CREATE TABLE `evolue_en` (
  `pokemon_base_id` int(11) NOT NULL,
  `pokemon_evol_id` int(11) NOT NULL,
  `niveau` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `evolue_en`
--

INSERT INTO `evolue_en` (`pokemon_base_id`, `pokemon_evol_id`, `niveau`) VALUES
(1, 2, 16),
(2, 3, 32),
(4, 5, 16),
(5, 6, 36),
(7, 8, 16),
(8, 9, 36),
(10, 11, 7),
(11, 12, 10),
(13, 14, 7),
(14, 15, 10),
(16, 17, 18),
(17, 18, 36),
(19, 20, 20),
(21, 22, 20),
(23, 24, 22),
(25, 26, -1),
(27, 28, 22),
(29, 30, 16),
(30, 31, -1),
(32, 33, 16),
(33, 34, -1),
(35, 36, -1),
(37, 38, -1),
(39, 40, -1),
(41, 42, 22),
(43, 44, 21),
(44, 45, -1),
(46, 47, 24),
(48, 49, 31),
(50, 51, 26),
(52, 53, 28),
(54, 55, 33),
(56, 57, 28),
(58, 59, -1),
(60, 61, 25),
(61, 62, -1),
(63, 64, 16),
(64, 65, -2),
(66, 67, 28),
(67, 68, -2),
(69, 70, 21),
(70, 71, -1),
(72, 73, 30),
(74, 75, 25),
(75, 76, -2),
(77, 78, 40),
(79, 80, 37),
(81, 82, 30),
(84, 85, 31),
(86, 87, 34),
(88, 89, 38),
(90, 91, -1),
(92, 93, 25),
(93, 94, -2),
(96, 97, 26),
(98, 99, 28),
(100, 101, 30),
(102, 103, -1),
(104, 105, 28),
(109, 110, 35),
(111, 112, 42),
(116, 117, 32),
(118, 119, 33),
(120, 121, -1),
(129, 130, 20),
(133, 134, -1),
(133, 135, -1),
(133, 136, -1),
(138, 139, 40),
(140, 141, 40),
(147, 148, 30),
(148, 149, 55);

-- --------------------------------------------------------

--
-- Structure de la table `pokemons`
--

CREATE TABLE `pokemons` (
  `id` int(11) NOT NULL,
  `nom` varchar(10) NOT NULL,
  `pv` int(11) NOT NULL DEFAULT 0,
  `pvMax` int(11) NOT NULL DEFAULT 0,
  `attaque` int(11) NOT NULL DEFAULT 0,
  `defense` int(11) NOT NULL DEFAULT 0,
  `vitesse` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `pokemons`
--

INSERT INTO `pokemons` (`id`, `nom`, `pv`, `pvMax`, `attaque`, `defense`, `vitesse`) VALUES
(1, 'Bulbizarre', 45, 45, 49, 49, 45),
(2, 'Herbizarre', 60, 60, 62, 63, 60),
(3, 'Florizarre', 80, 80, 82, 83, 80),
(4, 'Salameche', 39, 39, 52, 43, 65),
(5, 'Reptincel', 58, 58, 64, 58, 80),
(6, 'Dracaufeu', 78, 78, 84, 78, 100),
(7, 'Carapuce', 44, 44, 48, 65, 43),
(8, 'Carabaffe', 59, 59, 63, 80, 58),
(9, 'Tortank', 79, 79, 83, 100, 78),
(10, 'Chenipan', 45, 45, 30, 35, 45),
(11, 'Chrysacier', 50, 50, 20, 55, 30),
(12, 'Papilusion', 60, 60, 45, 50, 70),
(13, 'Aspicot', 40, 40, 35, 30, 50),
(14, 'Coconfort', 45, 45, 25, 50, 35),
(15, 'Dardargnan', 65, 65, 90, 40, 75),
(16, 'Roucool', 40, 40, 45, 40, 56),
(17, 'Roucoups', 63, 63, 60, 55, 71),
(18, 'Roucarnage', 83, 83, 80, 75, 91),
(19, 'Rattata', 30, 30, 56, 35, 72),
(20, 'Rattatac', 55, 55, 81, 60, 97),
(21, 'Piafabec', 40, 40, 60, 30, 70),
(22, 'Rapasdepic', 65, 65, 90, 65, 100),
(23, 'Abo', 35, 35, 60, 44, 55),
(24, 'Arbok', 60, 60, 95, 69, 80),
(25, 'Pikachu', 35, 35, 55, 40, 90),
(26, 'Raichu', 60, 60, 90, 55, 110),
(27, 'Sabelette', 50, 50, 75, 85, 40),
(28, 'Sablaireau', 75, 75, 100, 110, 65),
(29, 'NidoranF', 55, 55, 47, 52, 41),
(30, 'Nidorina', 70, 70, 62, 67, 56),
(31, 'Nidoqueen', 90, 90, 92, 87, 76),
(32, 'NidoranM', 46, 46, 57, 40, 50),
(33, 'Nidorino', 61, 61, 72, 57, 65),
(34, 'Nidoking', 81, 81, 102, 77, 85),
(35, 'Melofee', 70, 70, 45, 48, 35),
(36, 'Melodelfe', 95, 95, 70, 73, 60),
(37, 'Goupix', 38, 38, 41, 40, 65),
(38, 'Feunard', 73, 73, 76, 75, 100),
(39, 'Rondoudou', 115, 115, 45, 20, 20),
(40, 'Grodoudou', 140, 140, 70, 45, 45),
(41, 'Nosferapti', 40, 40, 45, 35, 55),
(42, 'Nosferalto', 75, 75, 80, 70, 90),
(43, 'Mystherbe', 45, 45, 50, 55, 30),
(44, 'Ortide', 60, 60, 65, 70, 40),
(45, 'Rafflesia', 75, 75, 80, 85, 50),
(46, 'Paras', 35, 35, 70, 55, 25),
(47, 'Parasect', 60, 60, 95, 80, 30),
(48, 'Mimitoss', 60, 60, 55, 50, 45),
(49, 'Aeromite', 70, 70, 65, 60, 90),
(50, 'Taupiqueur', 10, 10, 55, 25, 95),
(51, 'Triopikeur', 35, 35, 80, 50, 120),
(52, 'Miaouss', 40, 40, 45, 35, 90),
(53, 'Persian', 65, 65, 70, 60, 115),
(54, 'Psykokwak', 50, 50, 52, 48, 55),
(55, 'Akwakwak', 80, 80, 82, 78, 85),
(56, 'Ferosinge', 40, 40, 80, 35, 70),
(57, 'Colossinge', 65, 65, 105, 60, 95),
(58, 'Caninos', 55, 55, 70, 45, 60),
(59, 'Arcanin', 90, 90, 110, 80, 95),
(60, 'Ptitard', 40, 40, 40, 40, 90),
(61, 'Tetarte', 65, 65, 65, 65, 90),
(62, 'Tartard', 90, 90, 95, 95, 70),
(63, 'Abra', 25, 25, 20, 15, 90),
(64, 'Kadabra', 40, 40, 35, 30, 105),
(65, 'Alakazam', 55, 55, 50, 45, 120),
(66, 'Machoc', 70, 70, 80, 50, 35),
(67, 'Machopeur', 80, 80, 100, 70, 45),
(68, 'Mackogneur', 90, 90, 130, 80, 55),
(69, 'Chetiflor', 50, 50, 75, 35, 40),
(70, 'Boustiflor', 65, 65, 90, 50, 55),
(71, 'Empiflor', 80, 80, 105, 65, 70),
(72, 'Tentacool', 40, 40, 40, 35, 70),
(73, 'Tentacruel', 80, 80, 70, 65, 100),
(74, 'Racaillou', 40, 40, 80, 100, 20),
(75, 'Gravalanch', 55, 55, 95, 115, 35),
(76, 'Grolem', 80, 80, 120, 130, 45),
(77, 'Ponyta', 50, 50, 85, 55, 90),
(78, 'Galopa', 65, 65, 100, 70, 105),
(79, 'Ramoloss', 90, 90, 65, 65, 15),
(80, 'Flagadoss', 95, 95, 75, 110, 30),
(81, 'Magneti', 25, 25, 35, 70, 45),
(82, 'Magneton', 50, 50, 60, 95, 70),
(83, 'Canarticho', 52, 52, 90, 55, 60),
(84, 'Doduo', 35, 35, 85, 45, 75),
(85, 'Dodrio', 60, 60, 110, 70, 100),
(86, 'Otaria', 65, 65, 45, 55, 45),
(87, 'Lamantine', 90, 90, 70, 80, 70),
(88, 'Tadmorv', 80, 80, 80, 50, 25),
(89, 'Grotadmorv', 105, 105, 105, 75, 50),
(90, 'Kokiyas', 30, 30, 65, 100, 40),
(91, 'Crustabri', 50, 50, 95, 180, 70),
(92, 'Fantominus', 30, 30, 35, 30, 80),
(93, 'Spectrum', 45, 45, 50, 45, 95),
(94, 'Ectoplasma', 60, 60, 65, 60, 110),
(95, 'Onix', 35, 35, 45, 160, 70),
(96, 'Soporifik', 60, 60, 48, 45, 42),
(97, 'Hypnomade', 85, 85, 73, 70, 67),
(98, 'Krabby', 30, 30, 105, 90, 50),
(99, 'Krabboss', 55, 55, 130, 115, 75),
(100, 'Voltorbe', 40, 40, 30, 50, 100),
(101, 'Electrode', 60, 60, 50, 70, 140),
(102, 'Noeunoeuf', 60, 60, 40, 80, 40),
(103, 'Noadkoko', 95, 95, 95, 85, 55),
(104, 'Osselait', 50, 50, 50, 95, 35),
(105, 'Ossatueur', 60, 60, 80, 110, 45),
(106, 'Kicklee', 50, 50, 120, 53, 87),
(107, 'Tygnon', 50, 50, 105, 79, 76),
(108, 'Excelangue', 90, 90, 55, 75, 30),
(109, 'Smogo', 40, 40, 65, 95, 35),
(110, 'Smogogo', 65, 65, 90, 120, 60),
(111, 'Rhinocorne', 80, 80, 85, 95, 25),
(112, 'Rhinoferos', 105, 105, 130, 120, 40),
(113, 'Leveinard', 250, 250, 5, 5, 50),
(114, 'Saquedeneu', 65, 65, 55, 115, 60),
(115, 'Kangourex', 105, 105, 95, 80, 90),
(116, 'Hypotrempe', 30, 30, 40, 70, 60),
(117, 'Hypocean', 55, 55, 65, 95, 85),
(118, 'Poissirene', 45, 45, 67, 60, 63),
(119, 'Poissoroy', 80, 80, 92, 65, 68),
(120, 'Stari', 30, 30, 45, 55, 85),
(121, 'Staross', 60, 60, 75, 85, 115),
(122, 'M. Mime', 40, 40, 45, 65, 90),
(123, 'Insecateur', 70, 70, 110, 80, 105),
(124, 'Lippoutou', 65, 65, 50, 35, 95),
(125, 'Elektek', 65, 65, 83, 57, 105),
(126, 'Magmar', 65, 65, 95, 57, 93),
(127, 'Scarabrute', 65, 65, 125, 100, 85),
(128, 'Tauros', 75, 75, 100, 95, 110),
(129, 'Magicarpe', 20, 20, 10, 55, 80),
(130, 'Leviator', 95, 95, 125, 79, 81),
(131, 'Lokhlass', 130, 130, 85, 80, 60),
(132, 'Metamorph', 48, 48, 48, 48, 48),
(133, 'Evoli', 55, 55, 55, 50, 55),
(134, 'Aquali', 130, 130, 65, 60, 65),
(135, 'Voltali', 65, 65, 65, 60, 130),
(136, 'Pyroli', 65, 65, 130, 60, 65),
(137, 'Porygon', 65, 65, 60, 70, 40),
(138, 'Amonita', 35, 35, 40, 100, 35),
(139, 'Amonistar', 70, 70, 60, 125, 55),
(140, 'Kabuto', 30, 30, 80, 90, 55),
(141, 'Kabutops', 60, 60, 115, 105, 80),
(142, 'Ptera', 80, 80, 105, 65, 130),
(143, 'Ronflex', 160, 160, 110, 65, 30),
(144, 'Artikodin', 90, 90, 85, 100, 85),
(145, 'Electhor', 90, 90, 90, 85, 100),
(146, 'Sulfura', 90, 90, 100, 90, 90),
(147, 'Minidraco', 41, 41, 64, 45, 50),
(148, 'Draco', 61, 61, 84, 65, 70),
(149, 'Dracolosse', 91, 91, 134, 95, 80),
(150, 'Mewtwo', 106, 106, 110, 90, 130),
(151, 'Mew', 100, 100, 100, 100, 100);

-- --------------------------------------------------------

--
-- Structure de la table `types`
--

CREATE TABLE `types` (
  `id` int(11) NOT NULL,
  `libelle` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `types`
--

INSERT INTO `types` (`id`, `libelle`) VALUES
(1, 'Combat'),
(2, 'Dragon'),
(3, 'Eau'),
(4, 'Electrique'),
(5, 'Feu'),
(6, 'Glace'),
(7, 'Insecte'),
(8, 'Normal'),
(9, 'Plante'),
(10, 'Poison'),
(11, 'Psy'),
(12, 'Roche'),
(13, 'Sol'),
(14, 'Spectre'),
(15, 'Vol');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `attaques`
--
ALTER TABLE `attaques`
  ADD PRIMARY KEY (`id`),
  ADD KEY `type_id` (`type_id`);

--
-- Index pour la table `detient_pokemons`
--
ALTER TABLE `detient_pokemons`
  ADD KEY `dresseur_id` (`dresseur_id`),
  ADD KEY `pokemon_id` (`pokemon_id`);

--
-- Index pour la table `dresseurs`
--
ALTER TABLE `dresseurs`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `efficacite`
--
ALTER TABLE `efficacite`
  ADD PRIMARY KEY (`fkAtt`,`fkDef`),
  ADD KEY `cDef` (`fkDef`);

--
-- Index pour la table `est_type`
--
ALTER TABLE `est_type`
  ADD KEY `pokemon_id` (`pokemon_id`),
  ADD KEY `type_id` (`type_id`);

--
-- Index pour la table `evolue_en`
--
ALTER TABLE `evolue_en`
  ADD KEY `pokemon_base_id` (`pokemon_base_id`),
  ADD KEY `pokemon_evol_id` (`pokemon_evol_id`);

--
-- Index pour la table `pokemons`
--
ALTER TABLE `pokemons`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `types`
--
ALTER TABLE `types`
  ADD PRIMARY KEY (`id`);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `attaques`
--
ALTER TABLE `attaques`
  ADD CONSTRAINT `attaques_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `types` (`id`);

--
-- Contraintes pour la table `detient_pokemons`
--
ALTER TABLE `detient_pokemons`
  ADD CONSTRAINT `detient_pokemons_ibfk_1` FOREIGN KEY (`dresseur_id`) REFERENCES `dresseurs` (`id`),
  ADD CONSTRAINT `detient_pokemons_ibfk_2` FOREIGN KEY (`pokemon_id`) REFERENCES `pokemons` (`id`);

--
-- Contraintes pour la table `efficacite`
--
ALTER TABLE `efficacite`
  ADD CONSTRAINT `cAtt` FOREIGN KEY (`fkAtt`) REFERENCES `types` (`id`),
  ADD CONSTRAINT `cDef` FOREIGN KEY (`fkDef`) REFERENCES `types` (`id`);

--
-- Contraintes pour la table `est_type`
--
ALTER TABLE `est_type`
  ADD CONSTRAINT `est_type_ibfk_1` FOREIGN KEY (`pokemon_id`) REFERENCES `pokemons` (`id`),
  ADD CONSTRAINT `est_type_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `types` (`id`);

--
-- Contraintes pour la table `evolue_en`
--
ALTER TABLE `evolue_en`
  ADD CONSTRAINT `evolue_en_ibfk_1` FOREIGN KEY (`pokemon_base_id`) REFERENCES `pokemons` (`id`),
  ADD CONSTRAINT `evolue_en_ibfk_2` FOREIGN KEY (`pokemon_evol_id`) REFERENCES `pokemons` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
