<a name="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/Reap09/RKore">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">RKore</h3>

  <p align="center">
    Schematic Builder - Add schematics that place with a set duration and you can rotate the schematic via right clicking after placing the schem a gui will appear to change the direction and you may also cancel the placement and it will return the item to you! It has a set time you can configure in the config so it may take 10seconds to place or 10mins its up to you, you can add unlimited amount of cannon/build/anything you desire! (per schematic permissions coming soon), you can give the player a schembuilder via command, or make a ShopGUI+ shop! (DankOfUK will provide a free config for shopgui+)
Fly Boost Limiter - This is to stop the fly boosting on your server, players who use faction/pvp clients tent to have a fly boosting feature, as server owners this is hard to patch and ClearLagg doesn't have a staff bypass, RKore does have a staff bypass, and it works via blocks per second, so you can accurately determine the player speed and stop them via placing them back to their previous location that they flyboosted from, you can either set the player back and send a chat message, or toggle a option to enable a kick message and set player back, it is fully configurable!
Clear Lag - This is similar to ClearLag but is also has a few features that are unique, it has commands on clear, and customizable messages to send, and is toggleable to if you preview your clearlag option you can disable this.
Clear Inventory Confirm - A essentials feature to most servers to allow players to confirm they want to clear their inventory! can also be disabled if you use another plugin for such things!
Command Spy - A Simple but essentials feature for staff members to spy on players and what commands they are doing!

    <br />
    <a href="https://github.com/Reap_9/RKore"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/Reap09/RKore/issues">Report Bug</a>
    ·
    <a href="https://github.com/Reap09/RKore/issues">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-rkore">About RKore</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#features">Features</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#how-to-use">How to Use</a></li>
  </ol>
</details>



<!-- ABOUT RKORE -->
## About RKore

[![Product Name Screen Shot][product-screenshot]](https://example.com)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- BUILT WITH -->
### Built With

* [![Java][Java.com]][Java-url]
* [![MariaDB][Mariadb.org]][Mariadb-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This is how you download and successfully active the plugin!

<!-- INSTALLATION -->
### Installation

1. Download the latest jar from [Downloads](https://discord.gg/ADUPEArnb6)
2. Insert into your servers plugins folder and restart/start your server

<p align="right">(<a href="#readme-top">back to top</a>)</p>




<!-- FEATURES -->
## Features

- [ ] Schematic Builder
- [ ] Fly Boost Limiter
- [ ] Clear Lag
- [ ] Clear Inventory Confirm
- [ ] Command Spy
- [ ] External Command Fix
- [ ] Info Commands

See the [open issues](https://github.com/Reap09/RKore/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Commands -->
## Commands

- [ ] /rkore - plugin credits
- [ ] /rkore help - shows argument commands
- [ ] /rkore give <player> <schematic> [with_air] - gives schembuilder item to player with or without air within the schematic (true/false)
- [ ] /rkore external <cmd> - performs a command to console that can be used to fix bedrock issues (( view settings.yml for more information ))
- [ ] /cmdspy - toggles on and off command spy
- [ ] /clearlag - clears entities from world (( able to have commands performed to console on clearlag such as /mob-stacking killall ))
- [ ] /clearlag time - displays the time until the next clearlag
- [ ] /clearinventory - clears a players inventory and also has the option for players to have to confirm clear and bypass confirm permission
- [ ] /flyboostlimiter - toggles fly speed limiter bypass for players with the permission
- [ ] /discord - shows the servers discord
- [ ] /website - shows the servers website
- [ ] /store - shows the servers web store

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Permissions -->
## Permissions

- [ ] Clear Inventory
  - [ ] permission - none
  - [ ] bypass permission - rkore.clearinventory.admin
- [ ] Command Spy
  - [ ] permission - rkore.cmdspy
  - [ ] bypass permission - rkore.cmdspy.bypass
- [ ] Info Commands
  - [ ] permission - none
- [ ] External Commands
  - [ ] permission - rkore.externalcommands
- [ ] Clear Lag 
  - [ ] permission - rkore.clearlag.clear
  - [ ] time permission - none
- [ ] Fly Speed Limiter
  - [ ] bypass permission - rkore.flyspeedlimiter.bypass
  - [ ] bedrock bypass permission - rkore.flyspeedlimiter.bedrock
  - [ ] toggle permission - rkore.flyspeedlimiter.toggle.bypass
- [ ] Schematic Builder
  - [ ] give permission - rkore.schembuilder.admin

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Discord: Reap#0001

Email: contact@mysticnetwork.org

Project Link: [https://github.com/Reap09/RKore](https://github.com/Reap09/RKore)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[product-screenshot]: images/screenshot.png
[Mariadb.org]: https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white
[Mariadb-url]: https://mariadb.org/
[Java.com]: https://img.shields.io/badge/Java-f89820?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/en/
