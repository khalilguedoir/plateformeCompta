package com.example.demo.service;

import com.example.demo.model.Client;
import com.example.demo.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client update(Long id, Client clientDetails) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setName(clientDetails.getName());
                    client.setEmail(clientDetails.getEmail());
                    client.setPhonenumber(clientDetails.getPhonenumber());
                    client.setAdresse(clientDetails.getAdresse());
                    return clientRepository.save(client);
                })
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
    }

    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }
}
