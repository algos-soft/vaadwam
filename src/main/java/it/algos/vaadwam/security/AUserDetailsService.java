package it.algos.vaadwam.security;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.FlowVar;
import it.algos.vaadflow.modules.role.RoleService;
import it.algos.vaadflow.modules.utente.Utente;
import it.algos.vaadflow.modules.utente.UtenteService;
import it.algos.vaadflow.service.ABootService;
import it.algos.vaadwam.modules.croce.Croce;
import it.algos.vaadwam.modules.croce.CroceService;
import it.algos.vaadwam.modules.milite.Milite;
import it.algos.vaadwam.modules.milite.MiliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static it.algos.vaadflow.application.FlowVar.projectName;

/**
 * Implements the {@link UserDetailsService}.
 * <p>
 * This implementation searches for {@link Utente} entities by the e-mail address
 * supplied in the login screen.
 */
@Service
@Primary
@AIScript(sovrascrivibile = false)
public class AUserDetailsService implements UserDetailsService {

    //    private final ALogin login;
    private final UtenteService utenteService;

    private final MiliteService militeService;

    private final RoleService roleService;

    private final CroceService croceService;

    public PasswordEncoder passwordEncoder;

    /**
     * Istanza (@Scope = 'singleton') inietta da Spring <br>
     */
    @Autowired
    protected ABootService boot;


    @Autowired
    public AUserDetailsService(UtenteService utenteService, MiliteService militeService, RoleService roleService, CroceService croceService) {
        this.utenteService = utenteService;
        this.roleService = roleService;
        this.militeService = militeService;
        this.croceService = croceService;

        this.passwordEncoder = new BCryptPasswordEncoder();

    }// end of Spring constructor


    /**
     * Recovers the {@link Utente} from the database using the e-mail address supplied
     * in the login screen. If the user is found, returns a
     * {@link org.springframework.security.core.userdetails.User}.
     *
     * @param username User's e-mail address
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String passwordHash = "";
        Collection<? extends GrantedAuthority> authorities;
        //        Utente utente = (Utente) utenteService.findById(username);
        Utente utente = (Utente) utenteService.findByKeyUnica(username);
        //        Milite milite = militeService.findById(username);
        Milite milite = militeService.findByKeyUnica(username);
        Croce croce;

        if (milite == null && utente == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            if (milite != null) {
                if (milite.enabled) {
                    croce = milite.croce;
                    passwordHash = passwordEncoder.encode(milite.getPassword());
                    authorities = roleService.getAuthorities(milite);
                    FlowVar.layoutTitle = croce != null ? croce.getOrganizzazione().getDescrizione() + " - " + croce.getDescrizione() : projectName;
                    return new User(username, passwordHash, authorities);
                } else {
                    throw new UsernameNotFoundException(username + " non è più attivo");
                }
            } else {
                croce = (Croce) utente.company;
                passwordHash = passwordEncoder.encode(utente.getPassword());
                authorities = roleService.getAuthorities(utente);
                FlowVar.layoutTitle = croce != null ? croce.getOrganizzazione().getDescrizione() + " - " + croce.getDescrizione() : projectName;
                return new User(username, passwordHash, authorities);
            }// end of if/else cycle
        }// end of if/else cycle

    }// end of method


}// end of class